package com.prod.GreenValley.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.prod.GreenValley.wrapper.SalesItemForm;

public class BillInfoDTO {

    private Long saleId;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private List<SalesItemForm> items;

    public BillInfoDTO(){};

    public BillInfoDTO(Long saleId, LocalDateTime saleDate, BigDecimal totalAmount, List<SalesItemForm> items){
        System.out.println("---------------******** "+ saleId);
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.items = items;
    }


    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }



    public BigDecimal getTotalAmount() {
        return totalAmount;
    }


    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }


    public LocalDateTime getSaleDate() {
        return saleDate;
    }


    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }


    public List<SalesItemForm> getItems() {
        return items;
    }


    public void setItems(List<SalesItemForm> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        return "BillInfoDTO [saleId=" + saleId + ", saleDate=" + saleDate + ", totalAmount=" + totalAmount + ", items="
                + items + "]";
    }

    
    
}
