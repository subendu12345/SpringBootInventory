package com.prod.GreenValley.DTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.prod.GreenValley.wrapper.SalesItemForm;

public class BillInfoDTO {

    private Long saleId;
    private String billNumber;
    private Date saleDate;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private String paymentMethod;
    private String customerName;
    private String customerMobile;
    private String customerAddress;
    private List<SalesItemForm> items;

    public BillInfoDTO(){};

    public BillInfoDTO(Long saleId, Date saleDate, BigDecimal totalAmount, List<SalesItemForm> items){
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }


    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }



    public BigDecimal getTotalAmount() {
        return totalAmount;
    }


    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }


    public Date getSaleDate() {
        return saleDate;
    }


    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }


    public List<SalesItemForm> getItems() {
        return items;
    }


    public void setItems(List<SalesItemForm> items) {
        this.items = items;
    }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerMobile() { return customerMobile; }
    public void setCustomerMobile(String customerMobile) { this.customerMobile = customerMobile; }
    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }


    @Override
    public String toString() {
        return "BillInfoDTO [saleId=" + saleId + ", saleDate=" + saleDate + ", totalAmount=" + totalAmount + ", items="
                + items + "]";
    }

    
    
}
