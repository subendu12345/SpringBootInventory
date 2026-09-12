package com.prod.GreenValley.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prod.GreenValley.DTO.BillInfoDTO;
import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.service.SaleItemService;
import com.prod.GreenValley.service.SaleService;
import com.prod.GreenValley.wrapper.SalesForm;
import com.prod.GreenValley.wrapper.SalesItemForm;
import com.prod.GreenValley.DTO.SaleInfoDTO;
import com.prod.GreenValley.DTO.SaleItemDTO;
import com.prod.GreenValley.Entities.SaleItem;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Controller
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleItemService saleItemService;

    @PostMapping("/sale/save")
    @Transactional
    public String saveProducts(@ModelAttribute SalesForm salesForm, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        try {
            
            Sale sale = saleService.saveSaleItem(salesForm);
            saleItemService.saveItems(salesForm, sale);

           BillInfoDTO billInfoDTO = new BillInfoDTO(sale.getId(), sale.getSaleDate(), sale.getTotalAmount(), salesForm.getSalesItems());
           billInfoDTO.setBillNumber(sale.getBillNumber());
           billInfoDTO.setDiscountAmount(sale.getDiscountAmount());
           billInfoDTO.setTaxAmount(sale.getTaxAmount());
           billInfoDTO.setPaymentMethod(sale.getPaymentMethod());
           billInfoDTO.setCustomerName(sale.getCustomerName());
           billInfoDTO.setCustomerMobile(sale.getCustomerMobile());
           billInfoDTO.setCustomerAddress(sale.getCustomerAddress());
           redirectAttributes.addFlashAttribute("saleToDisplayInModal", billInfoDTO);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            redirectAttributes.addFlashAttribute("saleError", e.getMessage());
        }
        return "redirect:/sales";
    }


    @GetMapping("/sales")
    public String getSaleDetail(Model model){
        SalesForm salesForm = new SalesForm();
        salesForm.getSalesItems().add(new SalesItemForm());
        model.addAttribute("salesForm", salesForm);
        return "/sale/DalySale";
    }

    @GetMapping("/sale/api/history")
    @ResponseBody
    public List<SaleInfoDTO> getSaleHistory() {
        List<SaleInfoDTO> result = new ArrayList<>();
        for (Sale sale : saleService.getSaleHistory()) {
            List<SaleItemDTO> items = new ArrayList<>();
            for (SaleItem item : sale.getSaleItems()) {
                SaleItemDTO dto = new SaleItemDTO();
                dto.setSaleItemId(item.getId());
                dto.setQuantitySold(item.getQuantitySold());
                dto.setUnitPriceAtSale(item.getUnitPriceAtSale());
                dto.setBarcode(item.getBarcode());
                dto.setProductInfo(item.getProduct().getName());
                items.add(dto);
            }
            SaleInfoDTO dto = new SaleInfoDTO(sale.getId(), sale.getSaleDate(), sale.getTotalAmount(), items);
            dto.setBillNumber(sale.getBillNumber());
            dto.setCustomerName(sale.getCustomerName());
            dto.setCustomerMobile(sale.getCustomerMobile());
            dto.setPaymentMethod(sale.getPaymentMethod());
            dto.setDiscountAmount(sale.getDiscountAmount());
            dto.setTaxAmount(sale.getTaxAmount());
            result.add(dto);
        }
        return result;
    }

}
