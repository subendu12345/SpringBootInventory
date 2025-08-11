package com.prod.GreenValley.DTO;

import java.math.BigDecimal;

public class PurchaseItemDTO {
    private Long id;
    private int quantity;
    private BigDecimal price;
    private String productName;
    private Long productId;

    public PurchaseItemDTO(Long id, int quantity, BigDecimal price, String productName, Long productId){
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.productName=productName;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getProductName() {
        return productName;
    }

    public Long getProductId() {
        return productId;
    }

    
    
}
