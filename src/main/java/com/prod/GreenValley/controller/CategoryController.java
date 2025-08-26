package com.prod.GreenValley.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import com.prod.GreenValley.DTO.CategoryDto;
import com.prod.GreenValley.DTO.CategoryResponseDto;
import com.prod.GreenValley.Entities.MasterCategory;
import com.prod.GreenValley.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Displays the category management page with a list of all categories.
     * 
     * @param model The Spring MVC model to pass data to the view.
     * @return The name of the Thymeleaf template to render.
     */
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("categoryDto", new CategoryDto()); // Add a new DTO for the form
        return "category/CategoryManger";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute CategoryDto categoryDto) {
        categoryService.saveCategoryWithSubCategories(categoryDto);
        return "redirect:/category";
    }

    /**
     * NEW: API endpoint to fetch a single category by ID, used by the frontend for
     * the edit modal.
     * 
     * @param id The ID of the category.
     * @return A JSON representation of the category.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryDtoById(@PathVariable Long id) {
        return categoryService.findCategoryDtoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * NEW: Handles the form submission to update an existing category.
     * 
     * @param id          The ID of the category to update.
     * @param categoryDto The DTO with the updated data.
     * @return A redirect to the category list page.
     */
    @PostMapping("/{id}/update")
    public String updateCategory(@PathVariable Long id, @ModelAttribute CategoryDto categoryDto) {
        categoryService.updateCategory(id, categoryDto);
        return "redirect:/category";
    }

    /**
     * NEW: Handles the request to delete a category.
     * @param id The ID of the category to delete.
     * @return A redirect to the category list page.
     */
    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/category";
    }

}
