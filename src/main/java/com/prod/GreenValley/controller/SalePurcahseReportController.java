package com.prod.GreenValley.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.GreenValley.DTO.SaleInfoDTO;
import com.prod.GreenValley.DTO.SalePurcahseDTO;
import com.prod.GreenValley.Entities.PurchaseEntry;
import com.prod.GreenValley.Entities.Sale;
import com.prod.GreenValley.repository.PurchaseEntryRepo;
import com.prod.GreenValley.repository.SaleRepo;
import com.prod.GreenValley.wrapper.ReportRequest;

@Controller
@RequestMapping("/report")

public class SalePurcahseReportController {
    @GetMapping("/view")
    public String getReportForm(Model model){
        ReportRequest reportRequest = new ReportRequest();
        model.addAttribute("reportRequest", reportRequest);

        return "/report/report.html";
    }
    

    

}
