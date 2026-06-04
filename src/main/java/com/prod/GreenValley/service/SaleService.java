package com.prod.GreenValley.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.DTO.ProductSearchDTO;
import com.prod.GreenValley.DTO.SaleReportDTO;
import com.prod.GreenValley.Entities.PriceBook;
import com.prod.GreenValley.Entities.Product;
import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.repository.PriceBookRepo;
import com.prod.GreenValley.repository.ProductRepo;
import com.prod.GreenValley.repository.SaleRepo;
import com.prod.GreenValley.repository.SalesItemRepo;
import com.prod.GreenValley.wrapper.SalesForm;

@Service
public class SaleService {

    @Autowired
    private SaleRepo saleRepo;

    @Autowired
    private SalesItemRepo salesItemRepo;

    @Autowired
    private PriceBookRepo priceBookRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepo productRepo;

    public Sale saveSaleItem(SalesForm salesForm) {
        Sale sale = new Sale();
        sale.setPaymentMethod(salesForm.getPaymentMethod());
        sale.setTotalAmount(salesForm.getTotalAmount());
        sale.setSaleDate(salesForm.getSaleDate());
        saleRepo.save(sale);

        return sale;
    }

    public List<Sale> getSaleDataByDate(LocalDate saleDate, LocalDate endDate) {
        if (endDate == null) {
            return saleRepo.findSalesByDateRange(saleDate);
        }
        return saleRepo.findSalesByDateRange(saleDate, endDate);
    }

    public List<SaleReportDTO> getSaleReport(LocalDate startDate, LocalDate endDate, Long catId) {
        if (catId == null) {
            return saleRepo.getProductSaleSummary(startDate, endDate);
        }
        return saleRepo.getProductSaleSummaryByCategoryId(startDate, endDate, catId);
    }

    public void deleteSaleById(Long id) {
        saleRepo.deleteById(id);
    }

    public void deleteSaleItem(Long saleId, Long itemId) throws Exception {
        // Find the sale by its ID. If not found, throw a custom exception.
        Sale sale = saleRepo.findById(saleId).orElse(null);

        if (sale == null) {
            throw new Exception("Sale not found with ID: " + saleId);
        }

        // Find the specific item to delete within the sale's list of items.
        boolean removed = sale.getSaleItems().removeIf(item -> item.getId().equals(itemId));

        // If no item was removed, it means the item ID didn't match.
        if (!removed) {
            throw new Exception("Sale item not found with ID: " + itemId);
        }

        // Save the updated sale object, which will also update the items list.
        if (sale.getSaleItems().isEmpty()) {
            saleRepo.deleteById(sale.getId());
        } else {
            saleRepo.save(sale);
        }

    }

    public String upsertSaleItemByBarcodeAndQuantity(Long saleItemId, String barcode, Date saleDate, int quantity) {
        PriceBook pb = priceBookRepo.findByProductBarCode(barcode);
        if (pb == null) {
            return "Price Book not created with this barcode " + barcode;
        }

        List<ProductSearchDTO> productSearchDTOs = productService.searchProducts(pb.getProduct().getName());
        if (productSearchDTOs == null || productSearchDTOs.isEmpty()) {
            return "Product not found";
        }

        // For update, we subtract the old quantity first to check stock
        Long currentStock = productSearchDTOs.get(0).getStockOnHeand();
        if (saleItemId != null) {
            SaleItem existingItem = salesItemRepo.findById(saleItemId).orElse(null);
            if (existingItem != null) {
                currentStock += existingItem.getQuantitySold();
            }
        }

        if (currentStock < quantity) {
            return "Stock not available";
        }

        Sale sale;
        SaleItem item;

        if (saleItemId != null) {
            item = salesItemRepo.findById(saleItemId).orElse(null);
            if (item != null) {
                sale = item.getSale();
                item.setQuantitySold(quantity);
                item.setUnitPriceAtSale(BigDecimal.valueOf(pb.getProductPrice()));
                sale.setTotalAmount(BigDecimal.valueOf(pb.getProductPrice() * quantity));
                sale.setSaleDate(saleDate);
            } else {
                return "Sale item not found with ID: " + saleItemId;
            }
        } else {
            Product myProduct = productRepo.findById(pb.getProduct().getId()).orElse(null);
            sale = new Sale();
            sale.setTotalAmount(BigDecimal.valueOf(pb.getProductPrice() * quantity));
            sale.setSaleDate(saleDate);
            item = new SaleItem();
            item.setProduct(myProduct);
            item.setQuantitySold(quantity);
            item.setSale(sale);
            item.setUnitPriceAtSale(BigDecimal.valueOf(pb.getProductPrice()));
            item.setBarcode(barcode);
            sale.getSaleItems().add(item);
        }

        Sale savedSale = saleRepo.save(sale);
        // Find the item we just upserted in the saved sale to get its potentially new ID
        Long finalId = item.getId();
        if (finalId == null) {
            // This case happens for new items before flush, but save should have populated it.
            // If it's still null (unlikely with IDENTITY), try to find it.
            finalId = savedSale.getSaleItems().stream()
                .filter(si -> si.getBarcode().equals(barcode))
                .map(SaleItem::getId)
                .findFirst()
                .orElse(null);
        }
        return "success:" + finalId;
    }

    public String saveSaleItemByBarcode(String barcode, Date saleDate) {
        return saveSaleItemByBarcodeAndQuantity(barcode, saleDate, 1);
    }

    public String saveSaleItemByBarcodeAndQuantity(String barcode, Date saleDate, int quantity) {
        String res = upsertSaleItemByBarcodeAndQuantity(null, barcode, saleDate, quantity);
        if (res.startsWith("success:")) {
            return "success";
        }
        return res;
    }

}
