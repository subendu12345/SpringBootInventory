package com.prod.GreenValley.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.DTO.ProductSearchDTO;
import com.prod.GreenValley.DTO.ProductStockDTO;
import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.Entities.PurchaseEntryItem;
import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.repository.ProductRepo;
import com.prod.GreenValley.repository.PurchaseEntryItemRepo;
import com.prod.GreenValley.repository.SalesItemRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired 
    private ProductRepo productRepo;

    @Autowired
    private  PurchaseEntryItemRepo purchaseEntryItemRepo;

    @Autowired
    private SalesItemRepo salesItemRepo;

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
        return products.stream().map(product -> {
            // Calculate total quantity purchased for the product
            long totalPurchased = purchaseEntryItemRepo.findAll().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .mapToLong(PurchaseEntryItem::getQuantity)
                .sum();
            
            // Calculate total quantity sold for the product
            long totalSold = salesItemRepo.findAll().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .mapToLong(SaleItem::getQuantitySold)
                .sum();

            // Create and return a DTO with the stock data
            Long stockOnHand = totalPurchased  - totalSold;
            return new ProductSearchDTO(product.getId(), product.getName(), product.getPricePerUnit(), stockOnHand, (stockOnHand == 0 ? "Stock not avialable": ""));
        }).collect(Collectors.toList());
        // Map the list of Product entities to the new DTO
    }


    
}
