package com.prod.GreenValley.DTO;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;


public class SaleInfoDTO {
    private Long saleId;
    private String billNumber;
    private Date saleDate;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private String customerName;
    private String customerMobile;
    private String paymentMethod;
    private int itemCount;

    private List<SaleItemDTO> items;

    public SaleInfoDTO(Long saleId, Date saleDate, BigDecimal totalAmount, List<SaleItemDTO> items){
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.items = items;
        this.itemCount = items == null ? 0 : items.stream()
            .map(SaleItemDTO::getQuantitySold)
            .filter(quantity -> quantity != null)
            .mapToInt(Integer::intValue)
            .sum();

    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public List<SaleItemDTO> getItems() {
        return items;
    }

    public void setItems(List<SaleItemDTO> items) {
        this.items = items;
        this.itemCount = items == null ? 0 : items.stream()
            .map(SaleItemDTO::getQuantitySold)
            .filter(quantity -> quantity != null)
            .mapToInt(Integer::intValue)
            .sum();
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerMobile() { return customerMobile; }
    public void setCustomerMobile(String customerMobile) { this.customerMobile = customerMobile; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public int getItemCount() { return itemCount; }


    


}
