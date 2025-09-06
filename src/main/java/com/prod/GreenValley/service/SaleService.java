package com.prod.GreenValley.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.prod.GreenValley.wrapper.SalesForm;

@Service
public class SaleService {

    @Autowired
    private SaleRepo saleRepo;

    @Autowired
    private PriceBookRepo priceBookRepo;

    
    @Autowired
    private ProductService productService;
    

    @Autowired
    private ProductRepo productRepo;
    

    public Sale saveSaleItem(SalesForm salesForm){
        Sale sale = new Sale();
        sale.setPaymentMethod(salesForm.getPaymentMethod());
        sale.setTotalAmount(salesForm.getTotalAmount());
        sale.setSaleDate(salesForm.getSaleDate());
        saleRepo.save(sale);

        return sale;
    }

    public List<Sale> getSaleDataByDate(LocalDate saleDate, LocalDate endDate){
        if(endDate==null){
             return saleRepo.findSalesByDateRange(saleDate);
        }
        return saleRepo.findSalesByDateRange(saleDate, endDate);
    }

    public List<SaleReportDTO> getSaleReport(LocalDate startDate, LocalDate endDate, Long catId){
        if(catId == null){
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

        // Find the specific item to delete within the sale's list of items.
        boolean removed = sale.getSaleItems().removeIf(item -> item.getId().equals(itemId));

        // If no item was removed, it means the item ID didn't match.
        if (!removed) {
            throw new Exception("Sale item not found with ID: " + itemId);
        }

        // Save the updated sale object, which will also update the items list.
        if(sale.getSaleItems().isEmpty()){
            saleRepo.deleteById(sale.getId());
        }else{
            saleRepo.save(sale);
        }
        
    }

    public String saveSaleItemByBarcode(String barcode, Date saleDate){

        PriceBook pb = priceBookRepo.findByProductBarCode(barcode);
        if(pb == null){
           return "Price Book not created with this barcode "+barcode;
        }else{
            List<ProductSearchDTO> productSearchDTOs = productService.searchProducts(pb.getProduct().getName());
            if(productSearchDTOs != null && productSearchDTOs.get(0).getStockOnHeand() > 0){
                // insert sale;

                Product myProduct = productRepo.findById(pb.getProduct().getId()).orElse(null);
                Sale newSale = new Sale();
                newSale.setTotalAmount(BigDecimal.valueOf(pb.getProductPrice()));
                newSale.setSaleDate(saleDate);
                SaleItem newSaleItem  = new SaleItem();
                newSaleItem.setProduct(myProduct);
                newSaleItem.setQuantitySold(1);
                newSaleItem.setSale(newSale);
                newSaleItem.setUnitPriceAtSale(BigDecimal.valueOf(pb.getProductPrice()));
                newSale.getSaleItems().add(newSaleItem);
                newSaleItem.setBarcode(barcode);
                saleRepo.save(newSale);

            }else{
                return "Stock not available";
            }
            
        }
        return "success";

    }

    
}
