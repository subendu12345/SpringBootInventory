package com.prod.GreenValley.wrapper;

import java.math.BigDecimal;

public class SalesItemForm {
    private String productInfo;
    private Integer quantitySold;
    private BigDecimal unitPriceAtSale;
    private Long productId;
    private String barcode;

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BigDecimal getLineTotal() {
        if (unitPriceAtSale == null || quantitySold == null) {
            return BigDecimal.ZERO;
        }
        return unitPriceAtSale.multiply(BigDecimal.valueOf(quantitySold));
    }

    

}
