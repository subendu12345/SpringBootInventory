package com.prod.GreenValley.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.GreenValley.DTO.PriceBookDTO;
import com.prod.GreenValley.DTO.SaleInfoDTO;
import com.prod.GreenValley.DTO.SaleItemDTO;
import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.service.PricaeBookService;
import com.prod.GreenValley.service.SaleService;

@RestController
@RequestMapping("/sale")
public class SaleRestApiController {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date saleDate;

    @Autowired
    private SaleService saleService;

    @Autowired
    private PricaeBookService bookService;

    @GetMapping("/details/date")
    public List<SaleInfoDTO> getSaleDetailByDate(
        @RequestParam("date") LocalDate date,
        @RequestParam("endDate") LocalDate endDate) {
        System.out.println("dateString ============================================== " + endDate);
        List<Sale> saleList = saleService.getSaleDataByDate(date, endDate);
        List<SaleInfoDTO> saleDto = new ArrayList<>();
        for (Sale sl : saleList) {
            List<SaleItemDTO> saleItemDTOList = new ArrayList<>();
            for (SaleItem saleItem : sl.getSaleItems()) {
                SaleItemDTO dto = new SaleItemDTO();
                dto.setSaleItemId(saleItem.getId());
                dto.setQuantitySold(saleItem.getQuantitySold());
                dto.setUnitPriceAtSale(saleItem.getUnitPriceAtSale());
                dto.setProductInfo(saleItem.getProduct().getName());
                saleItemDTOList.add(dto);
            }
            SaleInfoDTO saleInfoDTO = new SaleInfoDTO(sl.getId(), sl.getSaleDate(), sl.getTotalAmount(),
                    saleItemDTOList);
            saleDto.add(saleInfoDTO);
        }
        System.out.println("saleDto   " + saleDto);
        return saleDto;

    }

    // Only ADMIN can delete.
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSale(@PathVariable Long id) {
        return "Sale with ID " + id + " deleted successfully by an admin.";
    }

    /**
     * Handles HTTP DELETE requests to remove a specific sale item.
     * 
     * @param saleId The ID of the sale containing the item.
     * @param itemId The ID of the specific item to delete.
     * @return a ResponseEntity with a success message.
     * @throws Exception
     */
    @DeleteMapping("/delete/{saleId}/items/{itemId}")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteSaleItem(
            @PathVariable Long saleId,
            @PathVariable Long itemId) throws Exception {
        System.out.println("valllllll");
        saleService.deleteSaleItem(saleId, itemId);
        return ResponseEntity.ok("Sale item deleted successfully.");
    }

    @PostMapping("/api/save/barcode/{barcode}/saledate/{saleDate}")
    public ResponseEntity<Map<String, String>> insertSaleByBarcode(@PathVariable String barcode, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date  saleDate) {
        System.out.println("barcode " + saleDate);
        String serviceResponse = saleService.saveSaleItemByBarcode(barcode, saleDate);
        System.out.println("serviceResponse "+ serviceResponse);
        
        
        //Create a Map to hold your key-value pairs for the JSON response
        Map<String, String> jsonResponse = Map.of("message", serviceResponse);

        // Return a ResponseEntity with the JSON Map and a successful HTTP status code
        return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
    }

    @GetMapping("/api/getproduct/barcode/{barcode}")
    public PriceBookDTO getProductInfoByBarcode(@PathVariable String barcode){
        System.out.println("barcode--------------------------->  "+ barcode);
        return bookService.getProductInfoByBarcode(barcode);

    }

}
