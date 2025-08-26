package com.prod.GreenValley.DTO;

import java.util.List;

public class CategoryResponseDto {
    private Long id;
    private String name;
    private List<SubCategoryResponseDto> subCategories;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCategoryResponseDto> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategoryResponseDto> subCategories) {
        this.subCategories = subCategories;
    }
}
