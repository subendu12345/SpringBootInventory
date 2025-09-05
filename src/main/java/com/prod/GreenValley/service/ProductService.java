package com.prod.GreenValley.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.DTO.PriceBookDTO;
import com.prod.GreenValley.DTO.ProductDTO;
import com.prod.GreenValley.DTO.ProductSearchDTO;
import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.Entities.PurchaseEntryItem;
import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.Entities.SubCategory;
import com.prod.GreenValley.repository.PriceBookRepo;
import com.prod.GreenValley.repository.ProductRepo;
import com.prod.GreenValley.repository.PurchaseEntryItemRepo;
import com.prod.GreenValley.repository.SalesItemRepo;
import com.prod.GreenValley.repository.SubCategoryRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private PricaeBookService bookService;

    @Autowired
    private PurchaseEntryItemRepo purchaseEntryItemRepo;

    @Autowired
    private SalesItemRepo salesItemRepo;

    @Autowired
    private SubCategoryRepo subCategoryRepo;

    public List<Product> findAllProduct() {
        return productRepo.findAll();
    }

    public Product findProductById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    public String doInsertProducts(List<Product> products) {
        String message = "success";
        try {
            productRepo.saveAll(products);
        } catch (Exception e) {
            // TODO: handle exception
            message = e.getMessage();
        }
        return message;
    }

    public void updateProduct(Long id, ProductDTO productDTO) {
        Product prod = productRepo.findById(id).orElse(null);
        if (prod != null && prod.getId() != null) {
            if (productDTO.getCategoryId() != null) {
                SubCategory subCategory = subCategoryRepo.findById(productDTO.getCategoryId()).orElse(null);
                prod.setSubCategory(subCategory);
            }

            prod.setName(productDTO.getName());
            prod.setVolumeMl(productDTO.getVolumeMl());

            productRepo.save(prod);
        }
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
            Long stockOnHand = totalPurchased - totalSold;
            return new ProductSearchDTO(product.getId(), product.getName(), product.getPricePerUnit(), stockOnHand,
                    (stockOnHand == 0 ? "Stock not avialable" : ""));
        }).collect(Collectors.toList());
        // Map the list of Product entities to the new DTO
    }

    public List<PriceBookDTO> getPriceBooksByProductId(Long productId) {
        return bookService.getPriceBooksByProductId(productId);
    }

}
