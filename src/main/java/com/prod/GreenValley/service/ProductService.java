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
import java.util.Map;
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

    @Autowired
    private ProductStockService productStockService;

    public List<Product> findAllProduct() {
        return productRepo.findAll();
    }

    public Product findProductById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    public Map<Long, Long> findStockQuantityByProduct() {
        return productStockService.getProductStock().stream()
            .collect(Collectors.toMap(
                stock -> stock.getId(),
                stock -> stock.getPurchaseQuantity() - stock.getSaleQuantity()
            ));
    }

    public Map<Long, Double> findLatestPriceByProduct() {
        return bookService.findLatestPriceByProduct();
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
            Long subCategoryId = productDTO.getSubCategoryId() != null
                ? productDTO.getSubCategoryId()
                : productDTO.getCategoryId();
            if (subCategoryId != null) {
                SubCategory subCategory = subCategoryRepo.findById(subCategoryId).orElse(null);
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
        
        if (products.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());

        // Get total quantities purchased grouped by product ID for these products
        List<Object[]> purchasedData = purchaseEntryItemRepo.getTotalQuantityByProductIds(productIds);
        java.util.Map<Long, Long> purchasedMap = purchasedData.stream()
            .collect(Collectors.toMap(
                data -> ((Number) data[1]).longValue(),
                data -> ((Number) data[0]).longValue()
            ));

        // Get total quantities sold grouped by product ID for these products
        List<Object[]> soldData = salesItemRepo.getTotalQuantitySoldByProductIds(productIds);
        java.util.Map<Long, Long> soldMap = soldData.stream()
            .collect(Collectors.toMap(
                data -> ((Number) data[1]).longValue(),
                data -> ((Number) data[0]).longValue()
            ));

        return products.stream().map(product -> {
            long totalPurchased = purchasedMap.getOrDefault(product.getId(), 0L);
            long totalSold = soldMap.getOrDefault(product.getId(), 0L);

            // Create and return a DTO with the stock data
            Long stockOnHand = totalPurchased - totalSold;
                ProductSearchDTO result = new ProductSearchDTO(product.getId(), product.getName(), product.getPricePerUnit(), stockOnHand,
                    (stockOnHand <= 0 ? "Stock not avialable" : ""));
                List<PriceBookDTO> priceBooks = bookService.getPriceBooksByProductId(product.getId());
                if (!priceBooks.isEmpty()) {
                result.setBarcode(priceBooks.get(priceBooks.size() - 1).getProductBarCode());
                }
                return result;
        }).collect(Collectors.toList());
    }

    public List<PriceBookDTO> getPriceBooksByProductId(Long productId) {
        return bookService.getPriceBooksByProductId(productId);
    }

}
