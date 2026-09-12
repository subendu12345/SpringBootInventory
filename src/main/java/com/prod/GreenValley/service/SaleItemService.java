package com.prod.GreenValley.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.DTO.SaleItemDTO;
import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.repository.ProductRepo;
import com.prod.GreenValley.repository.SalesItemRepo;
import com.prod.GreenValley.service.ProductStockService;
import com.prod.GreenValley.util.SaleItemRecord;
import com.prod.GreenValley.wrapper.SalesForm;
import com.prod.GreenValley.wrapper.SalesItemForm;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SaleItemService {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private SalesItemRepo salesItemRepo;

    @Autowired
    private ProductStockService productStockService;

    @Transactional
    public void saveItems(SalesForm salesForm, Sale sale){
        List<SaleItem> salesItems = new ArrayList<>();
        for(SalesItemForm salesItemForm : salesForm.getSalesItems()){
            SaleItem item = new SaleItem();
            Product prod =  productRepo.findById(salesItemForm.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found with Name: " + salesItemForm.getProductInfo()));
            int quantity = salesItemForm.getQuantitySold() == null ? 0 : salesItemForm.getQuantitySold();
            long availableStock = productStockService.getAvailableStockByProductId(prod.getId());
            if (quantity < 1 || availableStock < quantity) {
                throw new IllegalArgumentException("Insufficient stock for product: " + prod.getName());
            }
            item.setProduct(prod);
            item.setQuantitySold(quantity);
            item.setSale(sale);
            item.setUnitPriceAtSale(salesItemForm.getUnitPriceAtSale());
            item.setBarcode(salesItemForm.getBarcode());
            salesItems.add(item);
        }
        salesItemRepo.saveAll(salesItems);
    }


    public SaleItemRecord getTotalSaleAmount(){
        return salesItemRepo.getTotalSaleAmount();
    }

    public void updateSaleItem(SaleItemDTO saleItemDTO) {
        SaleItem si  = salesItemRepo.findById(saleItemDTO.getSaleItemId()).orElse(null);
        if (saleItemDTO.getQuantitySold() != null) {
            si.setQuantitySold(saleItemDTO.getQuantitySold());
        } else if (saleItemDTO.getUnitPriceAtSale() != null) {
            si.setUnitPriceAtSale(saleItemDTO.getUnitPriceAtSale());
        }
        salesItemRepo.save(si);
    }
    
}
