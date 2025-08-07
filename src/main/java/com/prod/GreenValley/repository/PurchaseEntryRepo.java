package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.GreenValley.Entities.PurchaseEntry;

public interface PurchaseEntryRepo extends JpaRepository<PurchaseEntry, Long>{
    
}
