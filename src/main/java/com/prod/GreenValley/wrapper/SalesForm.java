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
    List<SalesItemForm> salesItems;

    public SalesForm(){
        this.paymentMethod = "";
        this.saleDate = new Date();
        this.totalAmount = BigDecimal.ZERO;
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

    public List<SalesItemForm> getSalesItems() {
        return salesItems;
    }

    public void setSalesItems(List<SalesItemForm> salesItems) {
        this.salesItems = salesItems;
    }

    
    
}
