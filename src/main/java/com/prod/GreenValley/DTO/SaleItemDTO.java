package com.prod.GreenValley.DTO;

import java.math.BigDecimal;

public class SaleItemDTO {
    private Long saleItemId;
    private Integer quantitySold ;
    private BigDecimal unitPriceAtSale;
    private String productInfo;
    private String productType;
    private Integer volumeMl;


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
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public Integer getVolumeMl() {
        return volumeMl;
    }
    public void setVolumeMl(Integer volumeMl) {
        this.volumeMl = volumeMl;
    }


    

    
}
