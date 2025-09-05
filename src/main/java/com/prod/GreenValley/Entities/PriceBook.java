package com.prod.GreenValley.Entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "price_book")
@Data
public class PriceBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "product_barCode")
    private String productBarCode;

    @Column(nullable = false, name = "product_price")
    private double productPrice;

    @Column(name = "created_date")
    private LocalDate createdDate; // Or use java.util.Date / java.time.LocalDate

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    // Relationship 1: Child of Product (Many-to-One)
    // This creates a foreign key column 'product_id' in the 'price_books' table.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
        this.modifiedDate = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = LocalDate.now();
    }

}
