package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.GreenValley.Entities.PurchaseEntryItem;

public interface PurchaseEntryItemRepo extends JpaRepository<PurchaseEntryItem, Long> {
    
}
