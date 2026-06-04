package com.prod.GreenValley.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.DTO.ProductStockDTO;
import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.Entities.PurchaseEntryItem;
import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.repository.ProductRepo;
import com.prod.GreenValley.repository.PurchaseEntryItemRepo;
import com.prod.GreenValley.repository.SalesItemRepo;

@Service
public class ProductStockService {
    

    private final ProductRepo productRepo;
    private final PurchaseEntryItemRepo purchaseEntryItemRepo;
    private final SalesItemRepo salesItemRepo;

    @Autowired
    public ProductStockService(ProductRepo productRepo, PurchaseEntryItemRepo purchaseEntryItemRepo,  SalesItemRepo salesItemRepo){
        this.productRepo = productRepo;
        this.purchaseEntryItemRepo = purchaseEntryItemRepo;
        this.salesItemRepo = salesItemRepo;
    }


    // Method to get a list of all products with their stock information.
    public List<ProductStockDTO> getProductStock() {
        // Get all products
        List<Product> products = productRepo.findAll();

        // Get total quantities purchased grouped by product ID
        List<Object[]> purchasedData = purchaseEntryItemRepo.getTotalQuantityByProduct();
        java.util.Map<Long, Long> purchasedMap = purchasedData.stream()
            .collect(Collectors.toMap(
                data -> ((Number) data[1]).longValue(),
                data -> ((Number) data[0]).longValue()
            ));

        // Get total quantities sold grouped by product ID
        List<Object[]> soldData = salesItemRepo.getTotalQuantitySoldByProduct();
        java.util.Map<Long, Long> soldMap = soldData.stream()
            .collect(Collectors.toMap(
                data -> ((Number) data[1]).longValue(),
                data -> ((Number) data[0]).longValue()
            ));

        return products.stream().map(product -> {
            long totalPurchased = purchasedMap.getOrDefault(product.getId(), 0L);
            long totalSold = soldMap.getOrDefault(product.getId(), 0L);

            // Create and return a DTO with the stock data
            return new ProductStockDTO(product.getId(), product.getName(), totalPurchased, totalSold, product.getPricePerUnit());
        }).collect(Collectors.toList());
    }
}
