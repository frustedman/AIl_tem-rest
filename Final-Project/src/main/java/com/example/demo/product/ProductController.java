package com.example.demo.product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.eclipse.tags.shaded.org.apache.bcel.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.auction.AuctionService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth/prod")
public class ProductController {

    @Autowired
    private ProductService service;
    @Autowired
    private AuctionService aservice;

    @Value("${spring.servlet.multipart.location}")
    private String path;

    @GetMapping("/add")
    public String addFrom() {
        return "prod/add";
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(ProductDto dto) {
        ProductDto p = service.save(dto);
        if(p==null) {
        	return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping("/read-img")
    public ResponseEntity<byte[]> read_img(String img) {
        ResponseEntity<byte[]> result = null;
        System.out.println(img);
        File f = new File(path + img);
        System.out.println(f.isFile());
        HttpHeaders header = new HttpHeaders();
        try {
            header.add("Content-Type", Files.probeContentType(f.toPath()));
            result = new ResponseEntity<byte[]>(
                    FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @PutMapping("/edit")
    public ResponseEntity<String> edit(ProductDto dto) {
        service.save(dto);
        return ResponseEntity.ok("Successfully edit");
    }

    @DeleteMapping("/del/{num}")
    public ResponseEntity<String> del(int num) {
        ProductDto p = service.getProd(num);
        service.delProd(num);
        return ResponseEntity.ok("Successfully delete");
    }

    @GetMapping("/categories/{categories}")
    public ResponseEntity<ArrayList<ProductDto>> Categories(String categories) {
        return ResponseEntity.ok(service.getByCategories(categories));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ArrayList<ProductDto>> name(String name) {
    	return ResponseEntity.ok(service.getByName(name));
    }

    @GetMapping("/myprod")
    public ResponseEntity<ArrayList<ProductDto>> myProduct() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
    	ArrayList<ProductDto> list=new ArrayList<>();
    	for(ProductDto a:service.getBySeller(id)) {
    		if(aservice.getByProduct(Product.create(a)).isEmpty()) {
    			list.add(a);
    		}
    	}
        return ResponseEntity.ok(list);
    }


}
