package com.prod.GreenValley.repository;

import com.prod.GreenValley.Entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProduct_Id(Long productId);

    @Query("SELECT COALESCE(SUM(i.quantityOnHand), 0) FROM Inventory i WHERE i.product.id = :productId")
    Long getQuantityOnHandByProductId(@Param("productId") Long productId);

    @Query("SELECT i.product.id, SUM(i.quantityOnHand) FROM Inventory i GROUP BY i.product.id")
    List<Object[]> getQuantityOnHandByProduct();
}
