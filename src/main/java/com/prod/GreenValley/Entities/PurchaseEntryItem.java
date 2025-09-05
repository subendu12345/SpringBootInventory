package com.prod.GreenValley.Entities;


import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "purchase_entry_item")
public class PurchaseEntryItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private BigDecimal price;

    // A many-to-one relationship back to the PurchaseEntry entity.
    // The @JoinColumn specifies the foreign key column 'purchase_entry_id' in this table.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_entry_id", nullable = false)
    private PurchaseEntry purchaseEntry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PurchaseEntry getPurchaseEntry() {
        return purchaseEntry;
    }

    public void setPurchaseEntry(PurchaseEntry purchaseEntry) {
        this.purchaseEntry = purchaseEntry;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    } 
}
