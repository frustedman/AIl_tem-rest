package com.example.demo.product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.auction.AuctionService;

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
    public String add(ProductDto dto) {
        ProductDto p = service.save(dto);

        if (!dto.getF1().isEmpty()) {
            String oname1 = dto.getF1().getOriginalFilename();
            String img1 = p.getNum() + oname1;
            File f1 = new File(path + img1);
            try {
                dto.getF1().transferTo(f1); // 업로드 파일 복사
                p.setImg1(f1.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!dto.getF2().isEmpty()) {
            String oname2 = dto.getF2().getOriginalFilename();
            String img2 = p.getNum() + oname2;
            File f2 = new File(path + img2);
            try {
                dto.getF2().transferTo(f2); // 업로드 파일 복사
                p.setImg2(f2.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!dto.getF3().isEmpty()) {
            String oname3 = dto.getF3().getOriginalFilename();
            String img3 = p.getNum() + oname3;
            File f3 = new File(path + img3);
            try {
                dto.getF3().transferTo(f3); // 업로드 파일 복사
                p.setImg3(f3.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!dto.getF4().isEmpty()) {
            String oname4 = dto.getF4().getOriginalFilename();
            String img4 = p.getNum() + oname4;
            File f4 = new File(path + img4);
            try {
                dto.getF4().transferTo(f4); // 업로드 파일 복사
                p.setImg4(f4.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!dto.getF5().isEmpty()) {
            String oname5 = dto.getF5().getOriginalFilename();
            String img5 = p.getNum() + oname5;
            File f5 = new File(path + img5);
            try {
                dto.getF5().transferTo(f5); // 업로드 파일 복사
                p.setImg5(f5.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        service.save(p);
        return "/index_member";
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

    @PostMapping("/edit")
    public String edit(ProductDto dto) {
        ProductDto p = service.getProd(dto.getNum());
        p.setName(dto.getName());
        p.setImg1(dto.getImg1());
        p.setImg2(dto.getImg2());
        p.setImg3(dto.getImg3());
        p.setImg4(dto.getImg4());
        p.setImg5(dto.getImg5());
        p.setCategories(dto.getCategories());
        service.save(p);
        return "redirect:/";
    }

    @RequestMapping("/del")
    public String del(int num) {
        ProductDto p = service.getProd(num);
        service.delProd(num);
        File f = new File(path + p.getImg1());
        f.delete();
        return "redirect:/";
    }

    @GetMapping("/categories")
    public void Categories(String categories, ModelMap map) {
        map.addAttribute("list", service.getByCategories(categories));
    }

    @GetMapping("/name")
    public void name(String name, ModelMap map) {
        map.addAttribute("list", service.getByName(name));
    }

    @GetMapping("/myprod")
    public String myProduct(HttpSession session, ModelMap map) {
        String seller = (String) session.getAttribute("loginId");
    	ArrayList<ProductDto> l=service.getBySeller(seller);
    	System.out.println(l.toString());
    	ArrayList<ProductDto> list=new ArrayList<>();
    	for(ProductDto a:l) {
    		if(aservice.getByProduct(Product.create(a)).isEmpty()) {
    			list.add(a);
    		}
    	}
    	System.out.println(list.toString());
        map.addAttribute("list", list);
        
        return "prod/myprod";
    }


}
