package com.prod.GreenValley.Entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private String brand;

    @Column(name = "country_of_origin")
    private String countryOfOrigin;

    @Column(name = "volume_ml")
    private Integer volumeMl;

    @Column(name = "price_per_unit", precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(unique = true)
    private String barcode;

    @Column(name = "image_url")
    private String imageUrl;

    // New field: A product belongs to one Category.
    // This creates a foreign key column named 'category_id' in the 'products' table.
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "category_id")
    // private Category category;

    // New field: A product belongs to one SubCategory.
    // This creates a foreign key column named 'sub_category_id' in the 'products' table.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<SaleItem> saleItems;

    @OneToMany(mappedBy = "product")
    private List<PurchaseEntryItem> purchaseEntryItems;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}