package com.elice.proteinplus.category.service;

import com.elice.proteinplus.category.entity.Category;
import com.elice.proteinplus.category.entity.CategoryDTO;
import com.elice.proteinplus.category.entity.UpdateCategoryDTO;
import com.elice.proteinplus.category.repository.CategoryRepository;
import com.elice.proteinplus.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    public List<Category> findAllCategory() { return categoryRepository.findAllCategoryByParentIdAscNullsFirstCategoryIdAsc(); }

    //부모 카테고리 조회
    public List<Category> findParentCategory(){
        return categoryRepository.findParentCategory();
    }

    //자식 카테고리 조회
    public List<Category> findChildCategory(Long parent_id){
        return categoryRepository.findChildCategory(parent_id);
    }

    //특정 카테고리 조회
//    public Long findCategoryIdByNameAndParentId(String name, Long parent_id) { return categoryRepository.findCategoryIdByNameAndParentId(name, parent_id); }

    //자식 카테고리 id를 이용하여 부모 카테고리 id 조회
    //부모 카테고리가 child_id 로 들어갈 경우 null 값 리턴
    public Long findParentCategoryId(Long child_id) {

        Long parentId = categoryRepository.findParentCategoryId(child_id);

        if(parentId == null){
            return null;
        }

        return parentId;
    }

    //부모 카테고리 추가
    //부모 카테고리 명 중복일 경우 에러 코드 ㄱㄱ  수정할 때도
    public Category createParentCategory(String name){

        String duplicateCategoryName = categoryRepository.duplicateCategoryName(name);

        if(duplicateCategoryName != null){
            throw new RuntimeException("이미 존재하는 카테고리명입니다");
        }

        Category parentCategory = null;

        Category createCategory = new Category();
        createCategory.create(name, parentCategory);

        Category createdCategory = categoryRepository.save(createCategory);
        return createdCategory;
    }

    //자식 카테고리 추가
    public Category createChildCategory(CategoryDTO categoryDTO){

        String duplicateCategoryName = categoryRepository.duplicateCategoryName(categoryDTO.getName());

        if(duplicateCategoryName != null){
            throw new RuntimeException("이미 존재하는 카테고리명입니다");
        }

        String categoryName = categoryDTO.getName();
        Long parentId = categoryDTO.getParent_id();

        Category parentCategory = categoryRepository.findCategoryById(parentId);

        Category createCategory = new Category();
        createCategory.create(categoryName, parentCategory);

        Category createdCategory = categoryRepository.save(createCategory);
        return createdCategory;
    }

    //카테고리 수정 -> 이름만 변경가능
    //이미 있는 이름일 경우 수정 X - 중복체크 필요
    public Category updateCategory(UpdateCategoryDTO updateCategoryDTO){

        String duplicateCategoryName = categoryRepository.duplicateCategoryName(updateCategoryDTO.getName());

        if(duplicateCategoryName != null){
            throw new RuntimeException("이미 존재하는 카테고리명입니다");
        }

        //id를 이용하여 특정카테고리 조회
        Category updateCategory = categoryRepository.findCategoryById(updateCategoryDTO.getId());

        //특정 카테고리의 name 변경
        updateCategory.update(updateCategoryDTO);

        Category updatedCategory = categoryRepository.save(updateCategory);


        return updatedCategory;
    }

    //카테고리 삭제
    //카테고리 목록이 보여지는 상황에서 삭제를 하는 것이기에 null의 가능성 배제
    public void deleteCategory(Long id) {

        //자식 카테고리가 존재하는 부모 카테고리인 경우 삭제 불가
        List<Category> childCategories = categoryRepository.findCategoryByParentId(id);

        if(!childCategories.isEmpty()){
            throw new RuntimeException("해당 카테고리를 참조하는 카테고리가 있어 삭제할 수 없습니다");
        }

        //카테고리 id 아래에 상품이 없는 경우에만 카테고리 삭제
        Long productCount = productService.countByCategoryId(id);

        if(productCount > 0){
            throw new RuntimeException("상품이 존재하는 카테고리는 삭제할 수 없습니다");
        }

        categoryRepository.deleteById(id);
    }
}
