package com.prod.GreenValley.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.service.ProductService;
import com.prod.GreenValley.util.Message;
import com.prod.GreenValley.wrapper.ProductForm;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @GetMapping("/products/form")
    public String showProductForm(Model model) {
        ProductForm productForm = new ProductForm();
        productForm.getProducts().add(new Product()); // Add one empty product to start
        model.addAttribute("productForm", productForm);
        return "product/productEntryForm";
    }

    @PostMapping("/products/save")
    public String saveProducts(@ModelAttribute ProductForm productForm, Model model, HttpSession session) {
        // Here you would typically save the products to a database.
        // For this example, we'll just add them to the model to display.
        model.addAttribute("savedProducts", productForm.getProducts());

        List<Product> products = productForm.getProducts();
        System.out.println("product list "+ products);
        for(Product prd : products){
            prd.setName(prd.getName() + prd.getVolumeMl().toString());
        }
        String message = productService.doInsertProducts(products);
        System.out.println("---------------------- "+message);
        if(message=="success"){
            session.setAttribute("message", new Message("Product Successfully inserted.. ", "alert-success"));
            return "redirect:/home";
        }
        else{
            session.setAttribute("message", new Message(message, "alert-error"));
        }
        return "redirect:/home";
    }  

}
