package com.prod.GreenValley.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prod.GreenValley.DTO.PriceBookRecordDTO;
import com.prod.GreenValley.Entities.PriceBook;

public interface PriceBookRepo extends JpaRepository<PriceBook, Long> {

    // @Query("SELECT pb FROM PriceBook pb WHERE pb.productBarCode = :productBarCode")
    // PriceBook findPriceBookByBarCode(String productBarCode);

    List<PriceBook> findByProduct_Id(Long productId);
    
    /**
     * Finds a PriceBook by its product barcode.
     * Spring Data JPA automatically generates the query from the method name.
     * The `Optional` return type is used to handle cases where no PriceBook is found.
     *
     * @param productBarCode The barcode of the product.
     * @return An Optional containing the found PriceBook, or an empty Optional if none is found.
     */
    PriceBook findByProductBarCode(String productBarCode);


    String product_Find_query="""
                SELECT 
                    pb.id as id, pb.product_bar_code as productBarCode, pb.product_price as productPrice, p.id as productId, p.name as productName
                FROM price_book as pb  INNER JOIN product AS p ON pb.product_id = p.id 
                WHERE pb.product_bar_code = :barcode ;
            """;

    @Query(value = product_Find_query, nativeQuery = true)
    PriceBookRecordDTO getPriceBookByBarcode(String barcode);

    @Query(value = "SELECT DISTINCT pb FROM PriceBook pb  WHERE product_id = : productId;", nativeQuery = true)
    List<PriceBook> findPriceBookByProductId(Long productId);

}
