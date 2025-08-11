package com.prod.GreenValley.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prod.GreenValley.DTO.PurchaseDTO;
import com.prod.GreenValley.DTO.PurchaseItemDTO;
import com.prod.GreenValley.Entities.PurchaseEntry;
import com.prod.GreenValley.Entities.PurchaseEntryItem;
import com.prod.GreenValley.service.PurchaseEntryItemService;
import com.prod.GreenValley.service.PurchaseEntryService;
import com.prod.GreenValley.wrapper.PurchaseEntryForm;


import jakarta.servlet.http.HttpSession;

@Controller
public class PurchaseEntryController {

    @Autowired
    private PurchaseEntryService pEntryService;

    @Autowired
    private PurchaseEntryItemService itemService;

    @PostMapping("/purchase/save")
    public String saveProducts(@ModelAttribute PurchaseEntryForm purchaseEntryForm, Model model, HttpSession session) {
        // Here you would typically save the products to a database.
        // For this example, we'll just add them to the model to display.
     
        model.addAttribute("savedProducts", purchaseEntryForm.getItems());

        try {
            
            PurchaseEntry entry = pEntryService.insertEntry(purchaseEntryForm);
            itemService.insertPurchesItems(purchaseEntryForm, entry);
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        


        return "redirect:/home";
    }

    @GetMapping("/purchase/purchase-detail")
    public String getPurchaseDetail(){
        return "/purchase/purchaseDetail";
    }
}
