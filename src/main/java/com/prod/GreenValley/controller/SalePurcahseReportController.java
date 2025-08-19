package com.prod.GreenValley.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
