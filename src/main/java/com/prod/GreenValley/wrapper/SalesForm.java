package com.prod.GreenValley.wrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class SalesForm {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date saleDate;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String customerName;
    private String customerMobile;
    private String customerAddress;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private String notes;
    List<SalesItemForm> salesItems;

    public SalesForm(){
        this.paymentMethod = "";
        this.saleDate = new Date();
        this.totalAmount = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.salesItems = new ArrayList<>();
    }

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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerMobile() { return customerMobile; }
    public void setCustomerMobile(String customerMobile) { this.customerMobile = customerMobile; }
    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<SalesItemForm> getSalesItems() {
        return salesItems;
    }

    public void setSalesItems(List<SalesItemForm> salesItems) {
        this.salesItems = salesItems;
    }

    
    
}
