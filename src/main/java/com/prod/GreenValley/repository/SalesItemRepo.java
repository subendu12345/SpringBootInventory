package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.util.SaleItemRecord;

public interface SalesItemRepo extends JpaRepository<SaleItem, Long>{


    @Query(value = "SELECT SUM(quantity_sold * unit_price_at_sale) AS TotalSaleAmount FROM sale_item;", nativeQuery = true)
    SaleItemRecord getTotalSaleAmount();
    
}
