package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prod.GreenValley.Entities.PurchaseEntryItem;
import com.prod.GreenValley.util.PurchaseItemRecord;

public interface PurchaseEntryItemRepo extends JpaRepository<PurchaseEntryItem, Long> {


    @Query(value = "SELECT sum(quantity * price) as TotalPurchaseAmt from purchase_entry_item;", nativeQuery = true)
    PurchaseItemRecord getTotalPurchaseAmount();
    
}
