package com.prod.GreenValley.DTO;

import java.math.BigDecimal;

public class ProductSearchDTO {
    private Long id;
    private String name;
    private String barcode;
    private BigDecimal pricePerUnit;
    private Long stockOnHeand;
    private String message;

    public ProductSearchDTO(Long id, String name, BigDecimal pricePerUnit, Long stockOnHeand, String message) {
        this.id = id;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.stockOnHeand = stockOnHeand;
        this.message = message;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Long getStockOnHeand() {
        return stockOnHeand;
    }

    public String getMessage() {
        return message;
    }

    
    
}
