package com.prod.GreenValley.repository;

import java.time.LocalDate;
import java.util.Date;
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
    @Query("SELECT DISTINCT s FROM Sale s JOIN FETCH s.saleItems si WHERE s.saleDate = :saleDate2")
    List<Sale> findSalesByDateRange(LocalDate saleDate2);

    /**
     * Finds all Sale entities where the saleDate is within the specified date range.
     * The JPQL query now correctly binds the :startDate and :endDate parameters.
     * This method fixes the typo from the previous version.
     * @param startDate The start date for the query range.
     * @param endDate   The end date for the query range.
     * @return A list of Sale objects that occurred within the specified date range.
     */
    @Query("SELECT DISTINCT s FROM Sale s JOIN FETCH s.saleItems si WHERE s.saleDate >= :startDate AND s.saleDate <= :endDate")
    List<Sale> findSaleByTwoDate(LocalDate startDate, LocalDate endDate);
}
