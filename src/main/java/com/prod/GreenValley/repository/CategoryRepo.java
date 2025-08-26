package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.GreenValley.Entities.MasterCategory;

public interface CategoryRepo extends JpaRepository<MasterCategory, Long> {
    
}
