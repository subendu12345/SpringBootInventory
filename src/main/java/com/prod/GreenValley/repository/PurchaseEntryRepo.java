package com.prod.GreenValley.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prod.GreenValley.DTO.PurchaseEntryRecordDTO;
import com.prod.GreenValley.DTO.PurchaseReportDTO;
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



    // The SQL query provided by the user.
    // The `:startDate` and `:endDate` are named parameters that will be
    // bound to the method's arguments.
    String PURCHASE_REPORT_QUERY = """
        SELECT
            p.name as productName,
            SUM(pi.quantity) AS totalQuantity,
            SUM(pi.quantity * pi.price) AS totaPurchasePrice,
            SUM(pi.quantity * p.volume_ml) AS totalVolume,
            pe.date_of_purchase AS dateOfPurchase,
            pi.price AS unitPrice,
            ct.name AS categoryName
        FROM
            purchase_entrie AS pe
        INNER JOIN
            purchase_entry_item AS pi ON pe.id = pi.purchase_entry_id
        INNER JOIN
            product AS p ON pi.product_id = p.id
            INNER JOIN sub_category as sc on p.sub_category_id = sc.id
            LEFT JOIN category as ct on sc.category_id = ct.id
        WHERE
            pe.date_of_purchase >= :startDate AND pe.date_of_purchase <= :endDate
        GROUP BY
            pe.date_of_purchase,
            p.name,
            pi.price
        ORDER BY
            dateOfPurchase ASC;
        """;

		
        /**
     * Executes the sales report query for a given date range.
     * The `nativeQuery = true` annotation tells Spring to use raw SQL.
     * The `ProductSaleSummary.class` in `constructorExpression` maps the
     * query results to our record.
     *
     * @param startDate The start date of the report range.
     * @param endDate   The end date of the report range.
     * @return A list of `ProductSaleSummary` objects.
     */
    @Query(value = PURCHASE_REPORT_QUERY, nativeQuery = true)
	List<PurchaseReportDTO> getPurchaseReport(LocalDate startDate, LocalDate endDate);



    
    // The SQL query provided by the user.
    // The `:startDate` and `:endDate` are named parameters that will be
    // bound to the method's arguments.
    String PURCHASE_REPORT_QUERY2 = """
        SELECT
            p.name as productName,
            SUM(pi.quantity) AS totalQuantity,
            SUM(pi.quantity * pi.price) AS totaPurchasePrice,
            SUM(pi.quantity * p.volume_ml) AS totalVolume,
            pe.date_of_purchase AS dateOfPurchase,
            pi.price AS unitPrice,
            ct.name AS categoryName
        FROM
            purchase_entrie AS pe
        INNER JOIN
            purchase_entry_item AS pi ON pe.id = pi.purchase_entry_id
        INNER JOIN
            product AS p ON pi.product_id = p.id
            INNER JOIN sub_category as sc on p.sub_category_id = sc.id
            LEFT JOIN category as ct on sc.category_id = ct.id
        WHERE
            pe.date_of_purchase >= :startDate AND pe.date_of_purchase <= :endDate
            AND ct.id = :catId

        GROUP BY
            pe.date_of_purchase,
            p.name,
            pi.price
        ORDER BY
            dateOfPurchase ASC;
        """;

		
        /**
     * Executes the sales report query for a given date range.
     * The `nativeQuery = true` annotation tells Spring to use raw SQL.
     * The `ProductSaleSummary.class` in `constructorExpression` maps the
     * query results to our record.
     *
     * @param startDate The start date of the report range.
     * @param endDate   The end date of the report range.
     * @return A list of `ProductSaleSummary` objects.
     */
    @Query(value = PURCHASE_REPORT_QUERY2, nativeQuery = true)
	List<PurchaseReportDTO> getPurchaseReportByCategory(LocalDate startDate, LocalDate endDate, Long catId);

    //this method only return purchase
    @Query(value = "SELECT pe.id, pe.date_of_purchase AS dateOfPurchase, pe.supplier_info AS supplierInfo FROM purchase_entrie AS pe WHERE id = :id;", nativeQuery = true)
    PurchaseEntryRecordDTO getSinglePurchaseEntry(Long id);
    
}
