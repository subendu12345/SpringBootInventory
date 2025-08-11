package com.prod.GreenValley.DTO;

import java.math.BigDecimal;

public class ProductSearchDTO {
    private Long id;
    private String name;
    private BigDecimal pricePerUnit; 

    public ProductSearchDTO(Long id, String name, BigDecimal pricePerUnit) {
        this.id = id;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
    
}
