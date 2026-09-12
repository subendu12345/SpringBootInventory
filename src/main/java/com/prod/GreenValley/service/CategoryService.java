package com.prod.GreenValley.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import com.prod.GreenValley.DTO.CategoryDto;
import com.prod.GreenValley.DTO.CategoryResponseDto;
import com.prod.GreenValley.DTO.SubCategoryDto;
import com.prod.GreenValley.DTO.SubCategoryResponseDto;
import com.prod.GreenValley.Entities.MasterCategory;
import com.prod.GreenValley.Entities.SubCategory;
import com.prod.GreenValley.repository.CategoryRepo;
import com.prod.GreenValley.repository.SubCategoryRepo;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private SubCategoryRepo subCategoryRepo;

    @Autowired
    private SubCategoryService subCategoryService;

    /**
     * Retrieves all categories from the database.
     * 
     * @return a list of all Category entities.
     */
    public List<MasterCategory> findAllCategories() {
        return categoryRepo.findAll();
    }

    /**
     * NEW: Finds a single category by ID and maps it to a DTO for API responses.
     * @param id The ID of the category.
     * @return An Optional containing the CategoryResponseDto if found, otherwise empty.
     */
    public Optional<CategoryResponseDto> findCategoryDtoById(Long id) {
        return categoryRepo.findById(id).map(this::convertToDto);
    }

     private CategoryResponseDto convertToDto(MasterCategory category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        List<SubCategoryResponseDto> subCategoryDtos = category.getSubCategories().stream()
                .map(sub -> {
                    SubCategoryResponseDto subDto = new SubCategoryResponseDto();
                    subDto.setName(sub.getName());
                    subDto.setCode(sub.getCode());
                    subDto.setId(sub.getId());
                    return subDto;
                })
                .collect(Collectors.toList());
        dto.setSubCategories(subCategoryDtos);
        return dto;
    }


    /**
     * Creates and saves a new Category and its associated SubCategories.
     * The method is transactional to ensure data consistency.
     * 
     * @param categoryDto The DTO containing the category and sub-category names.
     * @return the newly saved Category entity.
     */
    @Transactional
    public MasterCategory saveCategoryWithSubCategories(CategoryDto categoryDto) {
        if (categoryDto == null || categoryDto.getName() == null || categoryDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
        String categoryName = categoryDto.getName().trim();
        if (categoryRepo.existsByNameIgnoreCase(categoryName)) {
            throw new IllegalArgumentException("Category already exists: " + categoryName);
        }

        MasterCategory category = new MasterCategory();
        category.setName(categoryName);

        // 2. Create and link the SubCategory entities from the DTO
        List<SubCategoryDto> submittedSubCategories = categoryDto.getSubCategories() == null
                ? java.util.Collections.emptyList() : categoryDto.getSubCategories();
        java.util.Set<String> names = new java.util.HashSet<>();
        java.util.Set<String> codes = new java.util.HashSet<>();
        List<SubCategory> subCategories = submittedSubCategories.stream()
                .filter(dto -> dto != null && dto.getName() != null && !dto.getName().trim().isEmpty())
                .map(dto -> {
                    SubCategory subCategory = new SubCategory();
                    String subCategoryName = dto.getName().trim();
                    String subCategoryCode = dto.getCode() == null ? "" : dto.getCode().trim();
                    if (subCategoryCode.isEmpty()) {
                        throw new IllegalArgumentException("Sub-category code is required for: " + subCategoryName);
                    }
                    if (!names.add(subCategoryName.toLowerCase()) || subCategoryRepo.existsByNameIgnoreCase(subCategoryName)) {
                        throw new IllegalArgumentException("Sub-category name already exists: " + subCategoryName);
                    }
                    if (!codes.add(subCategoryCode.toLowerCase()) || subCategoryRepo.existsByCodeIgnoreCase(subCategoryCode)) {
                        throw new IllegalArgumentException("Sub-category code already exists: " + subCategoryCode);
                    }
                    subCategory.setName(subCategoryName);
                    subCategory.setCode(subCategoryCode);
                    subCategory.setCategory(category); // Link to the parent category
                    return subCategory;
                })
                .collect(Collectors.toList());

        category.setSubCategories(subCategories);

        // 3. Save the parent Category, which cascades to save the SubCategories
        return categoryRepo.save(category);
    }


    /**
     * Updates an existing Category and its associated SubCategories.
     * This method fetches the existing category, updates its name, and replaces
     * its sub-categories with the new ones from the DTO.
     * @param id The ID of the category to update.
     * @param categoryDto The DTO with the new category data.
     */

    public void updateCategory(Long id, CategoryDto categoryDto) {
        MasterCategory existingCategory = new MasterCategory();
        
        existingCategory = categoryRepo.findById(id).orElse(existingCategory);
        if (existingCategory.getId() != null) {
            existingCategory.setName(categoryDto.getName());
            // Clear existing sub-categories to avoid duplicates and handle removals
            if(existingCategory.getSubCategories().size() > 0){
                subCategoryService.deleteSubCategoryByParentId(existingCategory.getId());
                //existingCategory.getSubCategories().clear();
            }
            
            // Create and add the new sub-categories from the DTO
            for (SubCategoryDto subCatType : categoryDto.getSubCategories()) {
                if(subCatType != null && subCatType.getName() != null && subCatType.getCode() !=null){
                    SubCategory subCategory = new SubCategory();
                    subCategory.setName(subCatType.getName().trim());
                    subCategory.setCode(subCatType.getCode() != null ? subCatType.getCode().trim() : null);
                    subCategory.setCategory(existingCategory);
                    existingCategory.getSubCategories().add(subCategory);
                }
                
            }
            
            categoryRepo.save(existingCategory);
        }
    }



        /**
     * Deletes a category by its ID. The operation cascades to delete associated sub-categories.
     * @param id The ID of the category to delete.
     */

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }
}
