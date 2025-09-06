package com.prod.GreenValley.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.DTO.PurchaseEntryRecordDTO;
import com.prod.GreenValley.DTO.PurchaseReportDTO;
import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.Entities.PurchaseEntry;
import com.prod.GreenValley.Entities.PurchaseEntryItem;

import com.prod.GreenValley.repository.ProductRepo;
import com.prod.GreenValley.repository.PurchaseEntryRepo;
import com.prod.GreenValley.wrapper.PurchaseEntryForm;
import com.prod.GreenValley.wrapper.PurchaseEntryItemForm;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PurchaseEntryService {
    @Autowired
    PurchaseEntryRepo purchaseEntryRepo;

    @Autowired
    private ProductRepo productRepo;


    public void insertEntry() {

    }

    public PurchaseEntry insertEntry(PurchaseEntryForm purchaseEntryForm) {
        PurchaseEntry entry = new PurchaseEntry();
        entry.setBillNumber(purchaseEntryForm.getBillNumber());
        entry.setDateOfPurchase(purchaseEntryForm.getDateOfPurchase());
        entry.setSupplierInfo(purchaseEntryForm.getSupplierInfo());
        entry.setTotalAmount(purchaseEntryForm.getTotalAmount());

        purchaseEntryRepo.save(entry);

        return entry;

    }

    public PurchaseEntry savePurchase(PurchaseEntryForm purchaseForm) {
        System.out.println("save method calll ");
        PurchaseEntry purchase = new PurchaseEntry();
        if (purchaseForm.getId() != null) {
            purchase = purchaseEntryRepo.findById(purchaseForm.getId()).orElse(purchase);
            purchase.getPurchaseEntryItems().clear(); // Clear existing items for updates
        } else {
            purchase.setBillNumber(purchaseForm.getBillNumber());
        }

        purchase.setDateOfPurchase(purchaseForm.getDateOfPurchase());
        purchase.setSupplierInfo(purchaseForm.getSupplierInfo());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (PurchaseEntryItemForm itemForm : purchaseForm.getItems()) {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++ " + itemForm.getProductId());
            PurchaseEntryItem item = new PurchaseEntryItem();
            Product prod = productRepo.findById(itemForm.getProductId()).orElseThrow(
                    () -> new EntityNotFoundException("Product not found with Name : " + itemForm.getProductInfo()));
            item.setPurchaseEntry(purchase);
            item.setProduct(prod);
            item.setQuantity(itemForm.getQuantity());
            item.setPrice(itemForm.getPrice());
            purchase.getPurchaseEntryItems().add(item);

            totalAmount = totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        purchase.setTotalAmount(totalAmount);

        return purchaseEntryRepo.save(purchase);
    }

    //this method help to delete the purchase

    public void deletePurchase(Long id){
        purchaseEntryRepo.deleteById(id);
    }

    public List<PurchaseEntry> getAllPurchases() {
        return purchaseEntryRepo.findAll();
    }

    public PurchaseEntry findPurchaseById(Long id) {
        return purchaseEntryRepo.findPurchaseEntryById(id);
    }

    public List<PurchaseReportDTO> getPurchaseReportByTimeSpan(LocalDate startDate, LocalDate endDate, Long catId){
        System.out.println("catId--------------->  "+ catId);
        if(catId == null){
             return purchaseEntryRepo.getPurchaseReport(startDate, endDate);
        }
       return purchaseEntryRepo.getPurchaseReportByCategory(startDate, endDate, catId);
    }

    public PurchaseEntryRecordDTO findSinglePurchaseEntryById(Long id){
        return purchaseEntryRepo.getSinglePurchaseEntry(id);
    }
}
