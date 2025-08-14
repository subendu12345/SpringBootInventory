package com.prod.GreenValley.wrapper;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;


public class PurchaseEntryForm {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfPurchase;
    private String supplierInfo;
    private BigDecimal totalAmount;
    private String billNumber;
    private Long id;

    private List<PurchaseEntryItemForm> items;

    public PurchaseEntryForm(){
        this.items = new ArrayList<>();
        this.dateOfPurchase = new Date();
        this.totalAmount = BigDecimal.ZERO;
        this.supplierInfo = "";
    }

    public List<PurchaseEntryItemForm> getItems() { return items; }
    
    public void setItems(List<PurchaseEntryItemForm> items) { this.items = items; }

    public String getBillNumber() {
        return billNumber;
    }


    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }


    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }


    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }


    public String getSupplierInfo() {
        return supplierInfo;
    }


    public void setSupplierInfo(String supplierInfo) {
        this.supplierInfo = supplierInfo;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }


    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PurchaseEntryForm [dateOfPurchase=" + dateOfPurchase + ", supplierInfo=" + supplierInfo
                + ", totalAmount=" + totalAmount + ", billNumber=" + billNumber + ", id=" + id + ", items=" + items
                + "]";
    }

    
    
    
}
