package com.prod.GreenValley.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prod.GreenValley.repository.CategoryRepo;
import com.prod.GreenValley.service.CategoryService;
import com.prod.GreenValley.wrapper.ReportRequest;

@Controller
@RequestMapping("/report")

public class SalePurcahseReportController {
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/view")
    public String getReportForm(Model model){
        ReportRequest reportRequest = new ReportRequest();
        model.addAttribute("reportRequest", reportRequest);
        model.addAttribute("categories", categoryService.findAllCategories());

        return "/report/report.html";
    }
    

    

}
