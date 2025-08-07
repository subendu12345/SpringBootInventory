package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.GreenValley.Entities.SaleItem;

public interface SalesItemRepo extends JpaRepository<SaleItem, Long>{
    
}
