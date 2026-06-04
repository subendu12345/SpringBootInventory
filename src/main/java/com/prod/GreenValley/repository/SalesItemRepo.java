package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.util.SaleItemRecord;

import java.util.List;

public interface SalesItemRepo extends JpaRepository<SaleItem, Long>{


    @Query(value = "SELECT sum(quantity_sold) as total_quantity_sold, product_id from sale_item group by product_id", nativeQuery = true)
    List<Object[]> getTotalQuantitySoldByProduct();

    @Query(value = "SELECT sum(quantity_sold) as total_quantity_sold, product_id from sale_item where product_id in :productIds group by product_id", nativeQuery = true)
    List<Object[]> getTotalQuantitySoldByProductIds(@org.springframework.data.repository.query.Param("productIds") List<Long> productIds);

    @Query(value = "SELECT SUM(quantity_sold * unit_price_at_sale) AS TotalSaleAmount FROM sale_item;", nativeQuery = true)
    SaleItemRecord getTotalSaleAmount();
    
}
