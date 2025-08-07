package com.prod.GreenValley.DTO;

import java.math.BigDecimal;

public class SaleItemDTO {
    private Long saleItemId;
    private Integer quantitySold ;
    private BigDecimal unitPriceAtSale;
    private String productInfo;
    public Long getSaleItemId() {
        return saleItemId;
    }
    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }
    public Integer getQuantitySold() {
        return quantitySold;
    }
    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }
    public BigDecimal getUnitPriceAtSale() {
        return unitPriceAtSale;
    }
    public void setUnitPriceAtSale(BigDecimal unitPriceAtSale) {
        this.unitPriceAtSale = unitPriceAtSale;
    }
    public String getProductInfo() {
        return productInfo;
    }
    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    
}
