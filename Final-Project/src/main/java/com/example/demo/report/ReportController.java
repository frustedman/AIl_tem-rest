package com.example.demo.report;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/auth/report")
public class ReportController {
    @Autowired
    private ReportService service;

    @PostMapping("/add")
    public ResponseEntity<Boolean> add(ReportDto dto) {
    	try {
    		service.save(dto);
    	}catch(Exception e) {
    		return ResponseEntity.ok(true);
    	}
        return ResponseEntity.badRequest().body(null);
    }
    @GetMapping("/list")
    public ResponseEntity<ArrayList<ReportDto>> list() {
    	return ResponseEntity.ok(service.findAll());
    }
}
