package com.prod.GreenValley.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.GreenValley.DTO.ProductSearchDTO;
import com.prod.GreenValley.DTO.ProductStockDTO;
import com.prod.GreenValley.service.ProductService;
import com.prod.GreenValley.service.ProductStockService;

@RestController
@RequestMapping("/api")
public class ProductStockController {
    

    private final ProductStockService stockService;

    @Autowired
    private ProductService productService;

    @Autowired
    public ProductStockController(ProductStockService stockService) {
        this.stockService = stockService;
    }

    // Handles GET requests to /api/stock and returns the stock data.
    @GetMapping("/stock")
    public List<ProductStockDTO> getStockData() {
        System.out.println("-----------------------------------------------------------------------");
        return stockService.getProductStock();
    }

    @GetMapping("/product/search")
    public List<ProductSearchDTO>  searchProducts(@RequestParam("query") String query) {
        return productService.searchProducts(query);
        // System.out.println("----- "+products);
        // return ResponseEntity.ok(products);
    }
}
