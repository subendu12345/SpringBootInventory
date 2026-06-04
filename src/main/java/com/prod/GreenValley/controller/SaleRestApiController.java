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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.GreenValley.DTO.PriceBookDTO;
import com.prod.GreenValley.DTO.SaleInfoDTO;
import com.prod.GreenValley.DTO.SaleItemDTO;
import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.Entities.SaleItem;
import com.prod.GreenValley.service.PricaeBookService;
import com.prod.GreenValley.service.SaleItemService;
import com.prod.GreenValley.service.SaleService;

@RestController
@RequestMapping("/sale")
public class SaleRestApiController {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date saleDate;

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleItemService itemService;

    @Autowired
    private PricaeBookService bookService;

    @GetMapping("/details/date")
    public List<SaleInfoDTO> getSaleDetailByDate(
        @RequestParam("date") LocalDate date,
        @RequestParam("endDate") LocalDate endDate) {
        List<Sale> saleList = saleService.getSaleDataByDate(date, endDate);
        List<SaleInfoDTO> saleDto = new ArrayList<>();
        for (Sale sl : saleList) {
            List<SaleItemDTO> saleItemDTOList = new ArrayList<>();
            for (SaleItem saleItem : sl.getSaleItems()) {
                SaleItemDTO dto = new SaleItemDTO();
                dto.setSaleItemId(saleItem.getId());
                if(saleItem.getProduct().getSubCategory() != null){
                    dto.setProductType(saleItem.getProduct().getSubCategory().getCategory().getName());
                }
                dto.setVolumeMl(saleItem.getProduct().getVolumeMl());
                
                dto.setQuantitySold(saleItem.getQuantitySold());
                dto.setUnitPriceAtSale(saleItem.getUnitPriceAtSale());
                dto.setProductInfo(saleItem.getProduct().getName());
                saleItemDTOList.add(dto);
            }
            SaleInfoDTO saleInfoDTO = new SaleInfoDTO(sl.getId(), sl.getSaleDate(), sl.getTotalAmount(),
                    saleItemDTOList);
            saleDto.add(saleInfoDTO);
        }
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
        saleService.deleteSaleItem(saleId, itemId);
        return ResponseEntity.ok("Sale item deleted successfully.");
    }

    @PostMapping("/api/save/barcode/{barcode}/saledate/{saleDate}")
    public ResponseEntity<Map<String, String>> insertSaleByBarcode(@PathVariable String barcode, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date  saleDate) {
        String serviceResponse = saleService.saveSaleItemByBarcode(barcode, saleDate);  
        
        //Create a Map to hold your key-value pairs for the JSON response
        Map<String, String> jsonResponse = Map.of("message", serviceResponse);

        // Return a ResponseEntity with the JSON Map and a successful HTTP status code
        return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
    }

    @GetMapping("/api/getproduct/barcode/{barcode}")
    public PriceBookDTO getProductInfoByBarcode(@PathVariable String barcode){
        return bookService.getProductInfoByBarcode(barcode);

    }

    @PostMapping("/api/bulk-save/saledate/{saleDate}")
    public ResponseEntity<List<Map<String, String>>> bulkInsertSaleByBarcode(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date saleDate,
            @RequestBody List<Map<String, Object>> bulkData) {

        List<Map<String, String>> results = new ArrayList<>();

        for (Map<String, Object> item : bulkData) {
            String barcode = (String) item.get("barcode");
            Integer quantity = (Integer) item.get("quantity");

            Long saleItemId = null;
            if (item.get("id") != null) {
                if (item.get("id") instanceof Integer) {
                    saleItemId = ((Integer) item.get("id")).longValue();
                } else if (item.get("id") instanceof Long) {
                    saleItemId = (Long) item.get("id");
                } else if (item.get("id") instanceof String && !((String)item.get("id")).isEmpty()) {
                    saleItemId = Long.parseLong((String) item.get("id"));
                }
            }

            String serviceResponse = saleService.upsertSaleItemByBarcodeAndQuantity(saleItemId, barcode, saleDate, quantity);

            String status = "error";
            String message = serviceResponse;
            String savedId = null;

            if (serviceResponse.startsWith("success")) {
                status = "success";
                String[] parts = serviceResponse.split(":");
                if (parts.length > 1) {
                    savedId = parts[1];
                }
                message = "success";
            }

            Map<String, String> result = new java.util.HashMap<>();
            result.put("barcode", barcode);
            result.put("status", status);
            result.put("message", message);
            if (savedId != null) {
                result.put("id", savedId);
            }
            results.add(result);
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PutMapping("/update/item")
    public String updateSaleItem(@RequestBody SaleItemDTO saleItemDTO){
        try {
            itemService.updateSaleItem(saleItemDTO);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
