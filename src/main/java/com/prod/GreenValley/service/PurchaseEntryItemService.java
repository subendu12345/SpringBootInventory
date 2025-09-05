package com.prod.GreenValley.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.Entities.PurchaseEntry;
import com.prod.GreenValley.Entities.PurchaseEntryItem;
import com.prod.GreenValley.repository.ProductRepo;
import com.prod.GreenValley.repository.PurchaseEntryItemRepo;
import com.prod.GreenValley.util.PurchaseItemRecord;
import com.prod.GreenValley.wrapper.PurchaseEntryForm;
import com.prod.GreenValley.wrapper.PurchaseEntryItemForm;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PurchaseEntryItemService {
    
    @Autowired
    private PurchaseEntryItemRepo entryItemRepo;

	@Autowired
	private ProductRepo productRepo;


    public void insertPurchesItems(List<PurchaseEntryItem> items){
        entryItemRepo.saveAll(items);
    }


    public void insertPurchesItems(PurchaseEntryForm purchaseEntryForm, PurchaseEntry entry){
		List<PurchaseEntryItem> piList = new ArrayList<>();

		for(PurchaseEntryItemForm pif : purchaseEntryForm.getItems()){
			PurchaseEntryItem pi =new PurchaseEntryItem();
			Product prod =  productRepo.findById(pif.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found with Name : " + pif.getProductInfo()));
			pi.setPrice(pif.getPrice());
			pi.setProduct(prod);
			pi.setQuantity(pif.getQuantity());
			pi.setPurchaseEntry(entry);
			pi.setBarcode(pif.getBarcode());
			piList.add(pi);
		}

		entryItemRepo.saveAll(piList);
    }

	public PurchaseItemRecord getTotalPurchesAmount(){
		return entryItemRepo.getTotalPurchaseAmount();
	}
}
