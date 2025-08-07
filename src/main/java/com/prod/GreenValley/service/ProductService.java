package com.prod.GreenValley.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.DTO.ProductSearchDTO;
import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.repository.ProductRepo;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired 
    private ProductRepo productRepo;

    public String doInsertProducts(List<Product> products){
        String message = "success";
        try {
            productRepo.saveAll(products);
        } catch (Exception e) {
            // TODO: handle exception
            message = e.getMessage();
        }
        return message;
    }





    public List<ProductSearchDTO> searchProducts(String query) {
        // Assuming your repository has a method to find products by name
        List<Product> products = productRepo.findByNameContainingIgnoreCase(query);
        
        // Map the list of Product entities to the new DTO
        return products.stream()
                .map(product -> new ProductSearchDTO(product.getId(), product.getName()))
                .collect(Collectors.toList());
    }


    
}
