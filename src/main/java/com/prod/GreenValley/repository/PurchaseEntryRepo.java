package com.prod.GreenValley.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prod.GreenValley.Entities.PurchaseEntry;


public interface PurchaseEntryRepo extends JpaRepository<PurchaseEntry, Long>{
    
    @Query("SELECT DISTINCT p FROM PurchaseEntry p JOIN FETCH p.purchaseEntryItems pi WHERE p.id = :id")
    PurchaseEntry findPurchaseEntryById(Long id);
    
}
