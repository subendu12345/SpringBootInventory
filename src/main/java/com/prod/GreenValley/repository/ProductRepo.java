package com.prod.GreenValley.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.prod.GreenValley.Entities.Product;

public interface ProductRepo extends JpaRepository<Product, Long>{
    

    @Query("SELECT p FROM Product p WHERE lower(p.name) LIKE lower(concat('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("""
            SELECT p FROM Product p
            LEFT JOIN p.subCategory subCategory
            LEFT JOIN subCategory.category category
            WHERE :query = ''
               OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(COALESCE(p.brand, '')) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(COALESCE(p.size, '')) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(COALESCE(p.barcode, '')) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(subCategory.name) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(category.name) LIKE LOWER(CONCAT('%', :query, '%'))
               OR EXISTS (
                   SELECT priceBook.id FROM PriceBook priceBook
                   WHERE priceBook.product = p
                     AND LOWER(priceBook.productBarCode) LIKE LOWER(CONCAT('%', :query, '%'))
               )
            ORDER BY p.id ASC
            """)
    Page<Product> searchForManager(@Param("query") String query, Pageable pageable);
}
