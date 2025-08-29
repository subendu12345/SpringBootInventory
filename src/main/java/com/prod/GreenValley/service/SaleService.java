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

    public void deleteSaleById(Long id) {
        saleRepo.deleteById(id);
    }

    public void deleteSaleItem(Long saleId, Long itemId) throws Exception {
        // Find the sale by its ID. If not found, throw a custom exception.
        Sale sale = saleRepo.findById(saleId).orElse(null);

        // Find the specific item to delete within the sale's list of items.
        boolean removed = sale.getSaleItems().removeIf(item -> item.getId().equals(itemId));

        // If no item was removed, it means the item ID didn't match.
        if (!removed) {
            throw new Exception("Sale item not found with ID: " + itemId);
        }

        // Save the updated sale object, which will also update the items list.
        if(sale.getSaleItems().isEmpty()){
            saleRepo.deleteById(sale.getId());
        }else{
            saleRepo.save(sale);
        }
        
    }

    
}
