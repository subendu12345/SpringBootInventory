package com.prod.GreenValley.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public class SaleInfoDTO {
    private Long saleId;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;

    private List<SaleItemDTO> items;

    public SaleInfoDTO(Long saleId, LocalDateTime saleDate, BigDecimal totalAmount, List<SaleItemDTO> items){
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

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<SaleItemDTO> getItems() {
        return items;
    }

    public void setItems(List<SaleItemDTO> items) {
        this.items = items;
    }


    


}
