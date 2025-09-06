package com.prod.GreenValley.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prod.GreenValley.DTO.BillInfoDTO;
import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.service.SaleItemService;
import com.prod.GreenValley.service.SaleService;
import com.prod.GreenValley.wrapper.SalesForm;

import jakarta.servlet.http.HttpSession;

@Controller
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleItemService saleItemService;

    @PostMapping("/sale/save")
    public String saveProducts(@ModelAttribute SalesForm salesForm, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        try {
            
            Sale sale = saleService.saveSaleItem(salesForm);
            saleItemService.saveItems(salesForm, sale);

           BillInfoDTO billInfoDTO = new BillInfoDTO(sale.getId(), sale.getSaleDate(), sale.getTotalAmount(), salesForm.getSalesItems());
           redirectAttributes.addFlashAttribute("saleToDisplayInModal", billInfoDTO);

        } catch (Exception e) {
            // TODO: handle exception

        }
        
        System.out.println("################");
        return "redirect:/home";
    }


    @GetMapping("/sale/details")
    public String getSaleDetail(){      
        return "/sale/DalySale";
    }

}
