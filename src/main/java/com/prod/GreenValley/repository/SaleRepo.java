package com.prod.GreenValley.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prod.GreenValley.DTO.SaleReportDTO;
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
    @Query("SELECT DISTINCT s FROM Sale s JOIN FETCH s.saleItems si WHERE s.saleDate = :saleDate2 ORDER BY si.id DESC")
    List<Sale> findSalesByDateRange(LocalDate saleDate2);

    @Query("SELECT DISTINCT s FROM Sale s JOIN FETCH s.saleItems si WHERE s.saleDate >= :starDate AND s.saleDate <= :endDate ORDER BY si.id DESC")
    List<Sale> findSalesByDateRange(LocalDate starDate, LocalDate endDate);

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



    // The SQL query provided by the user.
    // The `:startDate` and `:endDate` are named parameters that will be
    // bound to the method's arguments.
    String SALES_REPORT_QUERY = """
        SELECT
            p.name as productName,
            SUM(si.quantity_sold) AS totalQuantity,
            SUM(si.quantity_sold * si.unit_price_at_sale) AS totalSalePrice,
            SUM(si.quantity_sold * p.volume_ml) AS totalVolume,
            s.sale_date AS saleDate,
            si.unit_price_at_sale AS unitPrice,
            ct.name AS categoryName
        FROM
            sale AS s
        INNER JOIN
            sale_item AS si ON s.id = si.sale_id
        INNER JOIN
            product AS p ON si.product_id = p.id
            INNER JOIN sub_category as sc on p.sub_category_id = sc.id
            LEFT JOIN category as ct on sc.category_id = ct.id
        WHERE
            s.sale_date >= :startDate AND s.sale_date <= :endDate
        GROUP BY
            s.sale_date,
            p.name,
            si.unit_price_at_sale
        ORDER BY
            saleDate ASC;
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
    @Query(value = SALES_REPORT_QUERY, nativeQuery = true)
    List<SaleReportDTO> getProductSaleSummary(LocalDate startDate, LocalDate endDate);



    // The SQL query provided by the user.
    // The `:startDate` and `:endDate` are named parameters that will be
    // bound to the method's arguments.
    String SALES_REPORT_QUERY2 = """
        SELECT
            p.name as productName,
            SUM(si.quantity_sold) AS totalQuantity,
            SUM(si.quantity_sold * si.unit_price_at_sale) AS totalSalePrice,
            SUM(si.quantity_sold * p.volume_ml) AS totalVolume,
            s.sale_date AS saleDate,
            si.unit_price_at_sale AS unitPrice,
            ct.name AS categoryName
        FROM
            sale AS s
        INNER JOIN
            sale_item AS si ON s.id = si.sale_id
        INNER JOIN
            product AS p ON si.product_id = p.id
            INNER JOIN sub_category as sc on p.sub_category_id = sc.id
            LEFT JOIN category as ct on sc.category_id = ct.id
        WHERE
            s.sale_date >= :startDate AND s.sale_date <= :endDate
            AND ct.id = :ctId
        GROUP BY
            s.sale_date,
            p.name,
            si.unit_price_at_sale
        ORDER BY
            saleDate ASC;
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
    @Query(value = SALES_REPORT_QUERY2, nativeQuery = true)
    List<SaleReportDTO> getProductSaleSummaryByCategoryId(LocalDate startDate, LocalDate endDate, Long ctId);



}
