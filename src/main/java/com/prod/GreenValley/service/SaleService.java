package com.prod.GreenValley.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.repository.SaleRepo;
import com.prod.GreenValley.wrapper.SalesForm;

@Service
public class SaleService {

    @Autowired
    private SaleRepo saleRepo;

    public Sale saveSaleItem(SalesForm salesForm){
        Sale sale = new Sale();
        sale.setPaymentMethod(salesForm.getPaymentMethod());
        sale.setTotalAmount(salesForm.getTotalAmount());

        saleRepo.save(sale);

        return sale;
    }

    
}
