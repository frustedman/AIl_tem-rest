package com.example.demo.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/report")
public class ReportController {
    @Autowired
    private ReportService service;

    @PostMapping("/add")
    public String add(ReportDto dto) {
        service.save(dto);
        return "redirect:/auth/auction/detail?num="+dto.getAuction().getNum();
    }
    @RequestMapping("list")
    public String list(ModelMap map) {
        map.addAttribute("list", service.findAll());
        return "report/list";
    }
}
