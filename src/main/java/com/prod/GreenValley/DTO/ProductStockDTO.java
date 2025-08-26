package com.prod.GreenValley.DTO;

import java.math.BigDecimal;

public class ProductStockDTO {
    
    private Long id;
    private String name;
    private long purchaseQuantity;
    private long saleQuantity;
    private BigDecimal pricePerUnit;

    public ProductStockDTO(Long id, String name, long purchaseQuantity, long saleQuantity, BigDecimal pricePerUnit) {
        this.id = id;
        this.name = name;
        this.purchaseQuantity = purchaseQuantity;
        this.saleQuantity = saleQuantity;
        this.pricePerUnit = pricePerUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(long purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public long getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(long saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    

    //*****************Gater Seter****************** */

    
}
