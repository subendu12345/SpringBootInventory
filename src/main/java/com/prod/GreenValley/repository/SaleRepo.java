package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.GreenValley.Entities.Sale;

public interface SaleRepo extends JpaRepository<Sale, Long>{
    
}
