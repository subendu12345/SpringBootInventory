package com.prod.GreenValley.Entities;
import java.util.ArrayList;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "purchase_entrie")
public class PurchaseEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_purchase")
    private Date dateOfPurchase;

    @Column(name = "supplier_info")
    private String supplierInfo;

    @Column(name = "bill_number")
    private String billNumber;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    // A one-to-many relationship with PurchaseEntryItem.
    // 'mappedBy' indicates that the relationship is owned by the 'purchaseEntry' field in the PurchaseEntryItem entity.
    // 'cascade = CascadeType.ALL' ensures that any changes (save, update, delete) to the PurchaseEntry
    // are cascaded to its associated PurchaseEntryItems.
    @OneToMany(mappedBy = "purchaseEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseEntryItem> purchaseEntryItems = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<PurchaseEntryItem> getPurchaseEntryItems() {
        return purchaseEntryItems;
    }

    public void setPurchaseEntryItems(List<PurchaseEntryItem> purchaseEntryItems) {
        this.purchaseEntryItems = purchaseEntryItems;
    }
   
    
}
