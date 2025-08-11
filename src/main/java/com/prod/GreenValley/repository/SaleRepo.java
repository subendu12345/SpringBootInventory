package com.prod.GreenValley.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prod.GreenValley.Entities.Sale;

public interface SaleRepo extends JpaRepository<Sale, Long>{
    

    /**
     * Finds all Sale entities where the saleDate is within the specified LocalDateTime range.
     * The JPQL query filters sales by a date range, which is efficient for database queries.
     *
     * @param startOfDay The start of the date (e.g., 'YYYY-MM-DDT00:00:00')
     * @param endOfDay   The end of the date (e.g., 'YYYY-MM-DDT23:59:59')
     * @return A list of Sale objects that occurred on the specified day.
     */
    @Query("SELECT DISTINCT s FROM Sale s JOIN FETCH s.saleItems si WHERE s.saleDate2 = :saleDate2")
    List<Sale> findSalesByDateRange(String saleDate2);
}
