package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.prod.GreenValley.Entities.SubCategory;

public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {

    @Modifying // Tells Spring this is a data modification query
    @Transactional // Ensures the operation runs in a transaction
    @Query("DELETE FROM SubCategory s WHERE s.category.id = :parentId")
    void deleteSubcategoryByParentId(Long parentId);
    
}
