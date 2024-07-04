package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.auction.Auction;
import com.example.demo.dataroom.DataroomDto;
import com.example.demo.dataroom.DataroomService;
import com.example.demo.dataroom.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import com.example.demo.auction.AuctionDto;
import com.example.demo.auction.AuctionService;
import com.example.demo.product.Product;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/all")
public class AllAccessController {

	@Autowired
	private DataroomService service;
	@Autowired
	private ReplyService rservice;

	@Autowired
	private AuctionService aservice;

	@Value("${spring.servlet.multipart.location}")
	private String path;
	
	@PostMapping("/getbyprodname")
	public String list(String prodname,ModelMap map,HttpSession session) {
		System.out.println(prodname);
		ArrayList<AuctionDto> l =new ArrayList<>();
		ArrayList<AuctionDto> list=aservice.getByProdName(prodname);
		
		for(AuctionDto dto:list) {
			if(dto.getStatus().equals("경매중")) {
				l.add(dto);
			}
		}
		map.addAttribute("list", l);
		session.setAttribute("list", l);
		return "auction/list";
	}

	@ResponseBody
	@GetMapping("/ajaxcategories")
	public Map Ajaxcategories(Product.Categories categories,HttpSession session) {
		Auction.Type atype= (Auction.Type) session.getAttribute("atype");
		Map map=new HashMap();
		ArrayList<AuctionDto> list2 =aservice.getByStatus("경매중");
		ArrayList<AuctionDto> list=new ArrayList<>();
		if (atype == null) {
			for(AuctionDto dto:list2) {
				if (dto.getProduct().getCategories().equals(categories)) {
					list.add(dto);
				}
			}
		}else {
			for(AuctionDto dto:list2) {
				if (dto.getType().equals(atype) && dto.getProduct().getCategories().equals(categories)) {
					list.add(dto);
				}
			}

		}
		map.put("list", list);
		return map;
	}
	@GetMapping("/categories")
	public String categories(Product.Categories categories,ModelMap map) {
		ArrayList<AuctionDto> l =new ArrayList<>();
		System.out.println(categories);
		ArrayList<AuctionDto> list=aservice.getByProdCategories(categories);
		for(AuctionDto dto:list) {
			if(dto.getStatus().equals("경매중")) {
				l.add(dto);
			}
		}
		map.addAttribute("list", l);
		return "auction/list";
	}
	
	@GetMapping("/list")
	public String list(ModelMap map, HttpSession session, Auction.Type atype) {
		ArrayList<AuctionDto> list2 =aservice.getByStatus("경매중");
		ArrayList<AuctionDto> list=new ArrayList<>();
		if (atype == null) {
			list = list2;
		}else {
			for(AuctionDto dto:list2) {
				if(dto.getType().equals(atype)) {
					list.add(dto);
				}
			}
			session.setAttribute("auction_type", atype);
		}
		map.addAttribute("list", list);
		map.addAttribute("type",atype);
		return "auction/list";
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

	@GetMapping("/qalist")
	public String qalist(ModelMap map, @RequestParam(value = "type", defaultValue = "0")int type) {
		ArrayList<DataroomDto> list=service.findAll();
		for(DataroomDto dto:list){
			dto.setReplies(rservice.findAll(dto));
		}
		map.addAttribute("list", list);
		map.addAttribute("type", type);
		return "dataroom/list";
	}
}
