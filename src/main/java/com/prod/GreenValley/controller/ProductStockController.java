package com.prod.GreenValley.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.GreenValley.DTO.ProductSearchDTO;
import com.prod.GreenValley.DTO.ProductStockDTO;
import com.prod.GreenValley.service.EmailService;
import com.prod.GreenValley.service.ProductService;
import com.prod.GreenValley.service.ProductStockService;
import com.prod.GreenValley.service.PurchaseEntryItemService;
import com.prod.GreenValley.service.SaleItemService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api")
public class ProductStockController {
    

    private final ProductStockService stockService;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SaleItemService saleItemService;

    @Autowired
    private PurchaseEntryItemService purchaseEntryItemService;


    @Autowired
    public ProductStockController(ProductStockService stockService) {
        this.stockService = stockService;
    }

    // Handles GET requests to /api/stock and returns the stock data.
    @GetMapping("/stock")
    public List<ProductStockDTO> getStockData() throws MessagingException, IOException {
        List<ProductStockDTO> productStocks= stockService.getProductStock();
        //emailService.sendProductStockReport(productStocks, "surya.bakra@gmail.com");
        System.out.println("-----------------------------------------------------------------------");
        return productStocks;
    }

    @GetMapping("/product/search")
    public List<ProductSearchDTO>  searchProducts(@RequestParam("query") String query) {
        return productService.searchProducts(query);
        // System.out.println("----- "+products);
        // return ResponseEntity.ok(products);
    }

    @GetMapping("/stock/amount")
    public ResponseEntity<Map<String, BigDecimal>> getCurrentStockAmt(){
        Map<String, BigDecimal> jsonResponse = Map.of(
            "SaleAmount", saleItemService.getTotalSaleAmount().TotalSaleAmount(),
            "PurchaseAmount", purchaseEntryItemService.getTotalPurchesAmount().TotalPurchaseAmt()
            );
        return new ResponseEntity<>(jsonResponse, HttpStatus.FOUND);
    }
}
