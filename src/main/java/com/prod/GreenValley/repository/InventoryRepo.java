package com.prod.GreenValley.repository;

import com.prod.GreenValley.Entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepo extends JpaRepository<Inventory, Long> {

    @Query("SELECT i.product.id, SUM(i.quantityOnHand) FROM Inventory i GROUP BY i.product.id")
    List<Object[]> getQuantityOnHandByProduct();
}
