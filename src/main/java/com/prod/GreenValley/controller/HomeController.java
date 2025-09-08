package com.prod.GreenValley.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.service.DataBaseBackupService;
import com.prod.GreenValley.service.EmailService;
import com.prod.GreenValley.wrapper.ProductForm;
import com.prod.GreenValley.wrapper.PurchaseEntryForm;
import com.prod.GreenValley.wrapper.PurchaseEntryItemForm;
import com.prod.GreenValley.wrapper.SalesForm;
import com.prod.GreenValley.wrapper.SalesItemForm;

import jakarta.mail.MessagingException;


import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.ui.Model;

@Controller
public class HomeController {

    @Value("${backup.toAddress}")
    private String toAddress;

    @Autowired
    private DataBaseBackupService backupDatabase;

    @Autowired
    private EmailService emailService;

    // This is home page controller
    @GetMapping({ "/", "/home" })
    public String home(Model model) throws IOException, InterruptedException, MessagingException {
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

    private void sendDBBackupFile() {
        try {
            String backupFilePath = backupDatabase.createDatabaseBackup();

            // 2. Prepare and send the email
            String subject = "Green Valley - Database Backup " + java.time.LocalDate.now();
            String body = "Dear Admin,<br><br>The daily database backup has been successfully created and is attached to this email.<br><br>Regards,<br>Green Valley App";
            emailService.sendBackUpFileTOAdmin(toAddress, subject, body, backupFilePath);
        } catch (Exception e) {
            // TODO: handle exception

            System.out.println("backup send email failed: " + e.getMessage());
        }
    }


    @GetMapping("/backup")
    public ResponseEntity<Map<String, String>> doDataBaseBackup(){
        Map<String, String> jsonResponse = new HashMap<>();
        try {
             sendDBBackupFile();
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "backup sucessfully send to this email address "+ toAddress +" please check.");
        } catch (Exception e) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Faild to send backup "+ e.getMessage());
        }

        return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
       
    }

    @RequestMapping("/signin")
    public String getSigninPage() {
        return "/signin";
    }


    @GetMapping("/backup/manager")
    public String getBackupManager(){
        return "backup/backupManager.html";
    }



}
