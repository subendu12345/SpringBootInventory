package com.prod.GreenValley.Entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "sale")
@Data
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "sale_date")
    private Date saleDate;

    @Column(name="sale_date2")
    private String saleDate2;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method", updatable = false)
    private String paymentMethod;
    
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> saleItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.saleDate2 = getDateString();
    }

    private String getDateString(){
            Date utilDate = new Date(); // Your java.util.Date object
    LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    int year = localDate.getYear();
    int month = localDate.getMonthValue(); // 1-indexed
    int day = localDate.getDayOfMonth();

        return String.valueOf(year) +"-"+ String.valueOf(month)+ "-"+String.valueOf(day);
    }

}
