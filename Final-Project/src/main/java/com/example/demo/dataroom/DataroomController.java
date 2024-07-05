package com.example.demo.dataroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins="*")
@RestController
@RequestMapping("/auth/dataroom")
public class DataroomController {
    @Autowired
    private DataroomService service;


    @PostMapping("/add")
    public ResponseEntity<Boolean> add(DataroomDto dto){
        try {
        	service.save(dto);
        }catch(Exception e) {
        	return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(true);
    }
    
    @PatchMapping("/update")
    public ResponseEntity<Boolean> update(DataroomDto dto){
    	try {
        	service.save(dto);
        }catch(Exception e) {
        	return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/delete/{num}")
    public ResponseEntity<Boolean> delete(int num){
        try {
        	 service.del(num);
        }catch(Exception e) {
        	return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/detail/{num}")
    public ResponseEntity<DataroomDto> detail(int num){
        return ResponseEntity.ok(service.get(num));
    }
}
