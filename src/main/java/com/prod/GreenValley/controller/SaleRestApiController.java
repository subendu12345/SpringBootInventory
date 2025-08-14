package com.prod.GreenValley.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.GreenValley.DTO.SaleInfoDTO;
import com.prod.GreenValley.DTO.SaleItemDTO;
import com.prod.GreenValley.DTO.SalePurcahseDTO;
import com.prod.GreenValley.Entities.PurchaseEntry;
import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.repository.PurchaseEntryRepo;
import com.prod.GreenValley.repository.SaleRepo;
import com.prod.GreenValley.service.SaleService;

@RestController
@RequestMapping("/sale")
public class SaleRestApiController {

    
    @Autowired
    private SaleService saleService;
    
    @GetMapping("/details/date")
    public List<SaleInfoDTO> getSaleDetailByDate(@RequestParam("date")  LocalDate date){
        System.out.println("dateString ============================================== "+date);
        List<Sale> saleList = saleService.getSaleDataByDate(date);
        List<SaleInfoDTO> saleDto = new ArrayList<>();
        for(Sale sl : saleList){
            List<SaleItemDTO> saleItemDTOList = new ArrayList<>();
            for (SaleItem saleItem : sl.getSaleItems()) {
                SaleItemDTO dto = new SaleItemDTO();
                dto.setSaleItemId(saleItem.getId());
                dto.setQuantitySold(saleItem.getQuantitySold());
                dto.setUnitPriceAtSale(saleItem.getUnitPriceAtSale());
                dto.setProductInfo(saleItem.getProduct().getName());
                saleItemDTOList.add(dto);
            }
            SaleInfoDTO saleInfoDTO = new SaleInfoDTO(sl.getId(), sl.getSaleDate(), sl.getTotalAmount(), saleItemDTOList);
            saleDto.add(saleInfoDTO);
        }
        System.out.println("saleDto   "+ saleDto);
        return saleDto;
            
    }
}
