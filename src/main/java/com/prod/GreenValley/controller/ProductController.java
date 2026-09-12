package com.prod.GreenValley.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.prod.GreenValley.DTO.PriceBookDTO;
import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.service.CategoryService;
import com.prod.GreenValley.service.PricaeBookService;
import com.prod.GreenValley.service.ProductService;
import com.prod.GreenValley.wrapper.ProductForm;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PricaeBookService priceBookService;




    
    @GetMapping("/products/form")
    public String showProductForm(Model model) {
        ProductForm productForm = new ProductForm();
        productForm.getProducts().add(new Product()); // Add one empty product to start
        model.addAttribute("productForm", productForm);
        model.addAttribute("categories", categoryService.findAllCategories());
        return "product/productEntryForm";
    }

    @PostMapping("/products/save")
    @ResponseBody
    public Map<String, String> saveProducts(@ModelAttribute ProductForm productForm) {
        List<Product> products = productForm.getProducts();
        String message = productService.doInsertProducts(products);
        if ("success".equals(message)) {
            for (Product product : products) {
                if (product.getId() != null && product.getBarcode() != null
                        && !product.getBarcode().isBlank() && product.getPricePerUnit() != null) {
                    PriceBookDTO priceBook = new PriceBookDTO();
                    priceBook.setProductId(product.getId());
                    priceBook.setProductBarCode(product.getBarcode());
                    priceBook.setProductPrice(product.getPricePerUnit().doubleValue());
                    String priceBookMessage = priceBookService.savePriceBook(priceBook);
                    if (!"success".equals(priceBookMessage)) {
                        return Map.of("status", "error", "message", priceBookMessage);
                    }
                }
            }
            return Map.of("status", "success", "message", "Product successfully inserted.");
        }
        return Map.of("status", "error", "message", message);
    }

    @GetMapping("/product")
    public String getProductManager(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        int pageSize = Math.min(Math.max(size, 5), 50);
        int pageNumber = Math.max(page, 0);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> productPage = productService.searchForManager(query.trim(), pageable);
        ProductForm productForm = new ProductForm();
        productForm.getProducts().add(new Product());
        model.addAttribute("productForm", productForm);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("productQuery", query);
        model.addAttribute("productPageSize", pageSize);
        model.addAttribute("stockQuantities", productService.findStockQuantityByProduct());
        model.addAttribute("latestPrices", productService.findLatestPriceByProduct());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("priceBookForm", new PriceBookDTO());
        return "product/productManager";
    }



    @GetMapping("/product/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.findProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            //model.addAttribute("subCategories", subCategories);
            return "product-form";
        }
        return "redirect:/products"; // Redirect if product not found.
    }

}
