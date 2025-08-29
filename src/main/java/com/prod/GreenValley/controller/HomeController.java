package com.prod.GreenValley.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.wrapper.ProductForm;
import com.prod.GreenValley.wrapper.PurchaseEntryForm;
import com.prod.GreenValley.wrapper.PurchaseEntryItemForm;
import com.prod.GreenValley.wrapper.SalesForm;
import com.prod.GreenValley.wrapper.SalesItemForm;

import org.springframework.ui.Model;
@Controller
public class HomeController {

    //This is home page controller
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Product> products = new ArrayList<>();
        model.addAttribute("products", products);

        ProductForm productForm = new ProductForm();
        PurchaseEntryForm purchaseEntryForm = new PurchaseEntryForm();
        
        purchaseEntryForm.getItems().add(new PurchaseEntryItemForm()); // Add one empty item to start
        model.addAttribute("purchaseEntry", purchaseEntryForm);

        productForm.getProducts().add(new Product()); // Add one empty product to start
        model.addAttribute("productForm", productForm);

        SalesForm salesForm = new SalesForm();
        salesForm.getSalesItems().add(new SalesItemForm());

        model.addAttribute("salesForm", salesForm);
        model.addAttribute("saleToDisplayInModal", null);
        return "home";
    }

    @RequestMapping("/signin")
    public String getSigninPage(){
        return "/signin";
    }

    
}
