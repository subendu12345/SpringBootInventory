package com.prod.GreenValley.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.DTO.SaleReportDTO;
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
        sale.setSaleDate(salesForm.getSaleDate());
        saleRepo.save(sale);

        return sale;
    }

    public List<Sale> getSaleDataByDate(LocalDate saleDate){
        return saleRepo.findSalesByDateRange(saleDate);
    }

    public List<SaleReportDTO> getSaleReport(LocalDate startDate, LocalDate endDate){
        return saleRepo.getProductSaleSummary(startDate, endDate);
    }

    
}
