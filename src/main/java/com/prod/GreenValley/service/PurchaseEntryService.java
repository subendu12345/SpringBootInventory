package com.prod.GreenValley.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.Entities.PurchaseEntry;
import com.prod.GreenValley.repository.PurchaseEntryRepo;
import com.prod.GreenValley.wrapper.PurchaseEntryForm;

@Service
public class PurchaseEntryService {
    @Autowired PurchaseEntryRepo purchaseEntryRepo;


    public void insertEntry(){
        
    }

    public PurchaseEntry insertEntry(PurchaseEntryForm purchaseEntryForm){
        PurchaseEntry entry = new PurchaseEntry();
        entry.setBillNumber(purchaseEntryForm.getBillNumber());
        entry.setDateOfPurchase(purchaseEntryForm.getDateOfPurchase());
        entry.setSupplierInfo(purchaseEntryForm.getSupplierInfo());
        entry.setTotalAmount(purchaseEntryForm.getTotalAmount());

        purchaseEntryRepo.save(entry);

        return entry;

    }
    
}
