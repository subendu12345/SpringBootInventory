package com.prod.GreenValley.restController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.prod.GreenValley.DTO.CategoryResponseDto;
import com.prod.GreenValley.DTO.ProductDTO;
import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.service.CategoryService;
import com.prod.GreenValley.service.ProductService;

@RestController
@RequestMapping("/products/api")
public class ProductRESTApi {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/get/{id}")
    
    public ProductDTO getSingleProduct(@PathVariable Long id){
        Product  pro = productService.findProductById(id);

        ProductDTO  dto = new ProductDTO();
        dto.setName(pro.getName());
        if(pro.getSubCategory() != null && pro.getSubCategory().getId() != null){
            dto.setCategoryId(pro.getSubCategory().getId());
        }
        dto.setVolumeMl(pro.getVolumeMl());
        dto.setId(pro.getId());
        
        return dto;
    }

    @GetMapping("/get/{categoryId}/subcategories")
    public Optional<CategoryResponseDto> getCategoryId(@PathVariable Long categoryId){
        return categoryService.findCategoryDtoById(categoryId);
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO){
        String message = "success";
        System.out.println("productDTO "+ productDTO.getName()+ "       "+ productDTO.getVolumeMl());
        try {
            productService.updateProduct(id, productDTO);
        } catch (Exception e) {
            // TODO: handle exception
            message = e.getMessage();
        }
        return message;
    }
}
