package com.elice.proteinplus.category.repository;

import com.elice.proteinplus.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    //전체 카테고리 조회
    @Query("select c from Category c left join c.parent p order by p.id asc nulls first, c.id asc")
    List<Category> findAllCategoryByParentIdAscNullsFirstCategoryIdAsc();

    //부모 카테고리 조회
    @Query("select c from Category c left join c.parent p where p.id is null order by c.id asc")
    List<Category> findParentCategory();

    //자식 카테고리 조회
    @Query("select c from Category c left join c.parent p where p.id = :parent_id order by c.id asc")
    List<Category> findChildCategory(@Param("parent_id") Long parent_id);

    @Query("select c from Category c left join c.parent p where p.id = :parent_id order by c.id asc")
    List<Category> findCategoryByParentId(@Param("parent_id") Long parent_id);

    //category_id를 이용하여 특정 카테고리 조회
    Category findCategoryById(@Param("id") Long id);

    @Query("select c.id from Category c left join c.parent p where (p.id = :parent_id or :parent_id is null) and c.name = :name")
    Long findCategoryIdByNameAndParentId(@Param("name") String name, @Param("parent_id") Long parent_id);

    //category_id를 이용하여 부모 카테고리 id 조회
    @Query("select p.id from Category c left join c.parent p where c.id = :category_id")
    Long findParentCategoryId(@Param("category_id") Long child_id);

    //카테고리 이름 중복체크
    @Query("select c.name from Category c left join c.parent p where c.name = :name or p.name = :name")
    String duplicateCategoryName(@Param("name") String name);

}
