package com.prod.GreenValley.DTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseDTO {
    private Long id;
    private Date dateOfPurchase;
    private String supplierInfo;
    private String billNumber;
    private BigDecimal totalAmount;
    private String productsInfo;
    private List<PurchaseItemDTO> purchaseItemDTOs = new ArrayList<>();
    

    public PurchaseDTO(Long id, Date dateOfPurchase, String supplierInfo, String billNumber, BigDecimal totalAmount, String productsInfo){
        this.id = id;
        this.dateOfPurchase = dateOfPurchase;
        this.supplierInfo = supplierInfo;
        this.billNumber = billNumber;
        this.totalAmount = totalAmount;
        this.productsInfo = productsInfo;
    }

    public PurchaseDTO(Long id, Date dateOfPurchase, String supplierInfo, List<PurchaseItemDTO> purchaseEntryItems ){
        this.id = id;
        this.dateOfPurchase = dateOfPurchase;
        this.supplierInfo = supplierInfo;
        this.purchaseItemDTOs = purchaseEntryItems;
        
    }


    public Long getId() {
        return id;
    }


    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }


    public String getSupplierInfo() {
        return supplierInfo;
    }


    public String getBillNumber() {
        return billNumber;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<PurchaseItemDTO> getPurchaseEntryItems() {
        return purchaseItemDTOs;
    }

    public String getProductsInfo() {
        return productsInfo;
    }   
    
}
