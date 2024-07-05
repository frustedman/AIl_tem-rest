package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auction.Auction;
import com.example.demo.auction.AuctionDto;
import com.example.demo.auction.AuctionService;
import com.example.demo.dataroom.DataroomDto;
import com.example.demo.dataroom.DataroomService;
import com.example.demo.product.Product;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/all")
public class AllAccessController {

	@Autowired
	private DataroomService service;

	@Autowired
	private AuctionService aservice;

	@Value("${spring.servlet.multipart.location}")
	private String path;
	
	@GetMapping("/list")	
	public ResponseEntity<Map<String,AuctionDto>> getList(){
		ArrayList<AuctionDto> l = aservice.getAllByBids("경매중");
		Map<String,AuctionDto> map=new HashMap<>();
		ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getType().equals(Auction.Type.BLIND)) {
				l.get(i).setMax(l.get(i).getMin());
			}
			list.add(null);
			map.put("HBA" + (list.size()), l.get(i));
			if (list.size() > 5) {
				break;
			}
		}
		ArrayList<AuctionDto> l2 = aservice.getAll();
		ArrayList<String> list2 = new ArrayList<>();
		for (int i = 0; i < l2.size(); i++) {
			if (l2.get(i).getType().equals(Auction.Type.BLIND) && l2.get(i).getStatus().equals("경매중")) {
				list2.add(null);
				map.put("BA" + (list2.size()), l2.get(i));
			}
			if (list2.size() > 5) {
				break;
			}
		}
		ArrayList<AuctionDto> l3 = aservice.getAll();
		ArrayList<String> list3 = new ArrayList<>();
		for (int i = 0; i < l2.size(); i++) {
			if (l2.get(i).getType().equals(Auction.Type.EVENT) && l3.get(i).getStatus().equals("경매중")) {
				list3.add(null);
				map.put("EA" + (list3.size()), l3.get(i));
			}
			if (list3.size() > 5) {
				break;
			}
		}
		ArrayList<AuctionDto> l4 = aservice.getAll();
		ArrayList<String> list4 = new ArrayList<>();
		for (int i = 0; i < l4.size(); i++) {
			if (l4.get(i).getStatus().equals("경매중")) {
				list4.add(null);
				map.put("LA" + (list4.size()), l4.get(i));
			}
			if (list4.size() > 5) {
				break;
			}
		}
		return ResponseEntity.ok(map);
	}
	
	
	
	
	@PostMapping("/getbyprodname")
	public ResponseEntity<ArrayList<AuctionDto>> list(String prodname) {
		ArrayList<AuctionDto> l =new ArrayList<>();
		ArrayList<AuctionDto> list=aservice.getByProdName(prodname);
		
		for(AuctionDto dto:list) {
			if(dto.getStatus().equals("경매중")) {
				l.add(dto);
			}
		}		
		return ResponseEntity.ok(l);
	}
	@GetMapping("/ajaxcategories/{categories}")
	public ResponseEntity<ArrayList<AuctionDto>> Ajaxcategories(Product.Categories categories,HttpSession session) {
		Auction.Type atype= (Auction.Type) session.getAttribute("auction_type");
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
		return ResponseEntity.ok(list);
	}
	
	// 카테고리 잠들다	
	
	@GetMapping("/list/{atype}")
	public ResponseEntity<ArrayList<AuctionDto>> list(HttpSession session,@PathVariable Auction.Type atype) {
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
		return ResponseEntity.ok(list);
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
	public ResponseEntity<ArrayList<DataroomDto>> qalist() {
		ArrayList<DataroomDto> list=service.findAll();
		return ResponseEntity.ok(list);
	}
}
