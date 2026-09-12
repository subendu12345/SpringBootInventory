package com.prod.GreenValley.DTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SaleAdjustmentRequest {
    private List<ReturnItem> returns = new ArrayList<>();
    private List<AddItem> additions = new ArrayList<>();
    private String paymentMethod;

    public List<ReturnItem> getReturns() { return returns; }
    public void setReturns(List<ReturnItem> returns) { this.returns = returns == null ? new ArrayList<>() : returns; }
    public List<AddItem> getAdditions() { return additions; }
    public void setAdditions(List<AddItem> additions) { this.additions = additions == null ? new ArrayList<>() : additions; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public static class ReturnItem {
        private Long saleItemId;
        private String barcode;
        private Integer quantity;
        private BigDecimal unitPrice;

        public Long getSaleItemId() { return saleItemId; }
        public void setSaleItemId(Long saleItemId) { this.saleItemId = saleItemId; }
        public String getBarcode() { return barcode; }
        public void setBarcode(String barcode) { this.barcode = barcode; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }

    public static class AddItem {
        private String barcode;
        private Integer quantity;
        private BigDecimal unitPrice;

        public String getBarcode() { return barcode; }
        public void setBarcode(String barcode) { this.barcode = barcode; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }
}
