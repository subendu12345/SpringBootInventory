package com.prod.GreenValley.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.DTO.SaleInfoDTO;
import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.repository.SaleRepo;
import com.prod.GreenValley.wrapper.SalesForm;

@Service
public class SaleService {

    @Autowired
    private SaleRepo saleRepo;

    public Sale saveSaleItem(SalesForm salesForm){
        Sale sale = new Sale();
        System.out.println("saleDat2  *********************** "+salesForm.getSaleDate());
        sale.setPaymentMethod(salesForm.getPaymentMethod());
        sale.setTotalAmount(salesForm.getTotalAmount());
        saleRepo.save(sale);

        return sale;
    }

    public List<Sale> getSaleDataByDate(String saleDate){
        return saleRepo.findSalesByDateRange(saleDate);
    }

    
}
