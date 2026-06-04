package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prod.GreenValley.Entities.PurchaseEntryItem;
import com.prod.GreenValley.util.PurchaseItemRecord;

import java.util.List;

public interface PurchaseEntryItemRepo extends JpaRepository<PurchaseEntryItem, Long> {


    @Query(value = "SELECT sum(quantity) as total_quantity, product_id from purchase_entry_item group by product_id", nativeQuery = true)
    List<Object[]> getTotalQuantityByProduct();

    @Query(value = "SELECT sum(quantity) as total_quantity, product_id from purchase_entry_item where product_id in :productIds group by product_id", nativeQuery = true)
    List<Object[]> getTotalQuantityByProductIds(@org.springframework.data.repository.query.Param("productIds") List<Long> productIds);

    @Query(value = "SELECT sum(quantity * price) as TotalPurchaseAmt from purchase_entry_item;", nativeQuery = true)
    PurchaseItemRecord getTotalPurchaseAmount();
    
}
