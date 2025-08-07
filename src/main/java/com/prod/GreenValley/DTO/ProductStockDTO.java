package com.prod.GreenValley.DTO;

public class ProductStockDTO {
    
    private Long id;
    private String name;
    private long purchaseQuantity;
    private long saleQuantity;

    public ProductStockDTO(Long id, String name, long purchaseQuantity, long saleQuantity) {
        this.id = id;
        this.name = name;
        this.purchaseQuantity = purchaseQuantity;
        this.saleQuantity = saleQuantity;
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

    //*****************Gater Seter****************** */

    
}
