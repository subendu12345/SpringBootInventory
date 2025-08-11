package com.prod.GreenValley.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prod.GreenValley.DTO.PurchaseDTO;
import com.prod.GreenValley.DTO.PurchaseItemDTO;
import com.prod.GreenValley.Entities.PurchaseEntry;
import com.prod.GreenValley.Entities.PurchaseEntryItem;
import com.prod.GreenValley.service.PurchaseEntryService;
import com.prod.GreenValley.wrapper.PurchaseEntryForm;

@RestController
@RequestMapping("/api")
public class PurchaseEntryRESTApi {
    @Autowired
    private PurchaseEntryService pEntryService;

    @GetMapping("/purchases")
    @ResponseBody
    public List<PurchaseDTO> getAllPurchases() {
        List<PurchaseDTO> purchaseDTOs = new ArrayList<>();
        List<PurchaseEntry> purchases = pEntryService.getAllPurchases();
        for (PurchaseEntry purchaseEntry : purchases) {
            purchaseDTOs.add(new PurchaseDTO(purchaseEntry.getId(), purchaseEntry.getDateOfPurchase(),
                    purchaseEntry.getSupplierInfo(), purchaseEntry.getBillNumber(), purchaseEntry.getTotalAmount()));
        }

        return purchaseDTOs;
    }

    @GetMapping("/purchases/{id}")
    public PurchaseDTO getPurchaseById(@PathVariable Long id) {
        PurchaseEntry entry = pEntryService.findPurchaseById(id);
        List<PurchaseItemDTO> itemDTOs = new ArrayList<>();
        for (PurchaseEntryItem purchaseEntryItem : entry.getPurchaseEntryItems()) {
            itemDTOs.add(new PurchaseItemDTO(purchaseEntryItem.getId(), purchaseEntryItem.getQuantity(),
                    purchaseEntryItem.getPrice(), purchaseEntryItem.getProduct().getName(), purchaseEntryItem.getProduct().getId()));
        }

        PurchaseDTO purchaseDTO = new PurchaseDTO(entry.getId(), itemDTOs);
        return purchaseDTO;
    }

        // API endpoint to update an existing purchase.

}
