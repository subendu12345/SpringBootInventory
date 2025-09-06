package com.prod.GreenValley.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prod.GreenValley.DTO.PurchaseDTO;
import com.prod.GreenValley.DTO.PurchaseEntryRecordDTO;
import com.prod.GreenValley.DTO.PurchaseItemDTO;
import com.prod.GreenValley.DTO.PurchaseReportDTO;
import com.prod.GreenValley.DTO.SaleReportDTO;
import com.prod.GreenValley.Entities.PurchaseEntry;
import com.prod.GreenValley.Entities.PurchaseEntryItem;
import com.prod.GreenValley.service.PurchaseEntryService;
import com.prod.GreenValley.service.SaleService;
import com.prod.GreenValley.wrapper.PurchaseEntryForm;

@RestController
@RequestMapping("/api")
public class PurchaseEntryRESTApi {
    @Autowired
    private PurchaseEntryService pEntryService;

    @Autowired
    private SaleService saleService;

    @GetMapping("/purchases")
    @ResponseBody
    public List<PurchaseDTO> getAllPurchases() {
        List<PurchaseDTO> purchaseDTOs = new ArrayList<>();
        List<PurchaseEntry> purchases = pEntryService.getAllPurchases();
        for (PurchaseEntry purchaseEntry : purchases) {
            List<String> productNames = new ArrayList<>();
            for (PurchaseEntryItem purchaseEntryItem : purchaseEntry.getPurchaseEntryItems()) {
                productNames.add(purchaseEntryItem.getProduct().getName());
            }

            purchaseDTOs.add(new PurchaseDTO(purchaseEntry.getId(), purchaseEntry.getDateOfPurchase(),
                    purchaseEntry.getSupplierInfo(), purchaseEntry.getBillNumber(), purchaseEntry.getTotalAmount(),
                    String.join(",", productNames)));
        }

        return purchaseDTOs;
    }

    @GetMapping("/purchases/{id}")
    public PurchaseDTO getPurchaseById(@PathVariable Long id) {
        PurchaseEntry entry = pEntryService.findPurchaseById(id);
        List<PurchaseItemDTO> itemDTOs = new ArrayList<>();
        if (entry != null) {
            for (PurchaseEntryItem purchaseEntryItem : entry.getPurchaseEntryItems()) {
                itemDTOs.add(new PurchaseItemDTO(purchaseEntryItem.getId(), purchaseEntryItem.getQuantity(),
                    purchaseEntryItem.getPrice(), purchaseEntryItem.getProduct().getName(),
                    purchaseEntryItem.getProduct().getId()));
            }
            PurchaseDTO purchaseDTO = new PurchaseDTO(entry.getId(), entry.getDateOfPurchase(), entry.getSupplierInfo(),
                itemDTOs);
            return purchaseDTO;
        }else{
            PurchaseEntryRecordDTO purchaseEntryRecordDTO = pEntryService.findSinglePurchaseEntryById(id);
            PurchaseDTO purchaseDTO = new PurchaseDTO(id, purchaseEntryRecordDTO.dateOfPurchase(), purchaseEntryRecordDTO.supplierInfo(), itemDTOs);
            return purchaseDTO;
        }
        

    }

    // API endpoint to update an existing purchase.

    @PutMapping("/purchases/{id}")
    @ResponseBody
    public void updatePurchase(@PathVariable Long id, @RequestBody PurchaseEntryForm purchaseForm) {
        purchaseForm.setId(id); // Ensure the ID is set for update
        try {
            pEntryService.savePurchase(purchaseForm);

        } catch (Exception e) {
            System.out.println("exception " + e.getMessage());
        }
    }

    @DeleteMapping("/purchase/delete/{id}")
    public String deletePurchase(@PathVariable Long id) {
        String messString = "success";
        try {
            pEntryService.deletePurchase(id);
        } catch (Exception e) {
            messString = e.getMessage();
        }

        return messString;
    }

    @GetMapping("/report/sale/details")
    public List<SaleReportDTO> getSaleDetailByDate(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            @RequestParam("categoryId") String categoryId) throws SQLException {
        // Convert string dates to LocalDate objects for processing.
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        Long ctIt = null;
        if(categoryId != null && categoryId !=""){
          ctIt = Long.valueOf(categoryId);
        }
        
        return saleService.getSaleReport(startDate, endDate, ctIt);

    }

    @GetMapping("/report/purchase/details")
    private List<PurchaseReportDTO> getPurchaseReport(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            @RequestParam("categoryId") String categoryId) {
        // Convert string dates to LocalDate objects for processing.
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        Long ctIt = null;
        if(categoryId != null && categoryId !=""){
          ctIt = Long.valueOf(categoryId);
        }
        return pEntryService.getPurchaseReportByTimeSpan(startDate, endDate, ctIt);

    }

}
