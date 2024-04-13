package com.elice.proteinplus.category.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@NoArgsConstructor
@Table(name= "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // parent_id
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE) //부모 카테고리 삭제시 하위 카테고리 연달아 삭제
    private Category parent;


    public void create(String name, Category parent){
        this.name = name;
        this.parent = parent;
    }

    public void update(UpdateCategoryDTO updateCategoryDTO){
        this.name = updateCategoryDTO.getName();
    }

}
