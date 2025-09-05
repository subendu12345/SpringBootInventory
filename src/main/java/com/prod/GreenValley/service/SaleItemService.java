package com.prod.GreenValley.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.repository.ProductRepo;
import com.prod.GreenValley.repository.SalesItemRepo;
import com.prod.GreenValley.util.SaleItemRecord;
import com.prod.GreenValley.wrapper.SalesForm;
import com.prod.GreenValley.wrapper.SalesItemForm;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SaleItemService {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private SalesItemRepo salesItemRepo;

    public void saveItems(SalesForm salesForm, Sale sale){
        List<SaleItem> salesItems = new ArrayList<>();
        for(SalesItemForm salesItemForm : salesForm.getSalesItems()){
            SaleItem item = new SaleItem();
            Product prod =  productRepo.findById(salesItemForm.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found with Name: " + salesItemForm.getProductInfo()));
            item.setProduct(prod);
            item.setQuantitySold(salesItemForm.getQuantitySold());
            item.setSale(sale);
            item.setUnitPriceAtSale(salesItemForm.getUnitPriceAtSale());
            salesItems.add(item);
        }
        salesItemRepo.saveAll(salesItems);
    }


    public SaleItemRecord getTotalSaleAmount(){
        return salesItemRepo.getTotalSaleAmount();
    }
    
}
