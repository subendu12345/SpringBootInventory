package com.prod.GreenValley.DTO;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {
    private String name;
    private List<SubCategoryDto> subCategories = new ArrayList<>();

    // Standard getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<SubCategoryDto> getSubCategories() { return subCategories; }
    public void setSubCategories(List<SubCategoryDto> subCategories) { this.subCategories = subCategories; }
}
