package com.prod.GreenValley.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prod.GreenValley.Entities.PurchaseEntry;


public interface PurchaseEntryRepo extends JpaRepository<PurchaseEntry, Long>{
    
   /**
     * Finds a single PurchaseEntry by its ID, eagerly fetching its purchaseEntryItems.
     * This query is correct and avoids potential N+1 query problems when accessing the items.
     * @param id The ID of the PurchaseEntry to find.
     * @return The PurchaseEntry with the specified ID.
     */
    @Query("SELECT DISTINCT p FROM PurchaseEntry p JOIN FETCH p.purchaseEntryItems pi WHERE p.id = :id")
    PurchaseEntry findPurchaseEntryById(Long id);

    /**
     * Finds all PurchaseEntry entities within a specified date range,
     * eagerly fetching their purchaseEntryItems. This query is correctly written.
     * @param startDate The beginning of the date range.
     * @param endDate   The end of the date range.
     * @return A list of PurchaseEntry objects within the specified date range.
     */
    @Query("SELECT DISTINCT p FROM PurchaseEntry p JOIN FETCH p.purchaseEntryItems pi WHERE p.dateOfPurchase >= :startDate AND p.dateOfPurchase <= :endDate")
    List<PurchaseEntry> findPurchaseEntryByTwoDate(LocalDate startDate, LocalDate endDate);
    
}
