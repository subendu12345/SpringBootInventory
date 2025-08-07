package com.prod.GreenValley.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.prod.GreenValley.Entities.Product;

public class ProductForm {
    private List<Product> products;

    public ProductForm() {
        this.products = new ArrayList<>();
    }

    // Getters and Setters
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
