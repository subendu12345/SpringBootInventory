package com.prod.GreenValley.DTO;

import java.util.List;

import com.prod.GreenValley.Entities.PurchaseEntry;
import com.prod.GreenValley.Entities.Sale;

public class SalePurcahseDTO {

    private List<Sale> sales;
    private List<PurchaseEntry> purchaseEntry;

    public SalePurcahseDTO(List<Sale> sales, List<PurchaseEntry> purchaseEntry){
        this.sales = sales;
        this.purchaseEntry = purchaseEntry;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public List<PurchaseEntry> getPurchaseEntry() {
        return purchaseEntry;
    }

    public void setPurchaseEntry(List<PurchaseEntry> purchaseEntry) {
        this.purchaseEntry = purchaseEntry;
    }


    

    
}
