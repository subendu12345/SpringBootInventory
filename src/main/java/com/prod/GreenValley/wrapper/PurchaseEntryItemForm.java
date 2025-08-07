package com.prod.GreenValley.wrapper;

import java.math.BigDecimal;

public class PurchaseEntryItemForm {
    
    private String productInfo;
    private int quantity;
    private BigDecimal price;
    private Long productId;

    //Getter and setter
    public String getProductInfo() {
        return productInfo;
    }
    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    

}
