package com.example.demo.scrap;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auction.Auction;
import com.example.demo.auction.AuctionService;
import com.example.demo.user.Member;
import com.example.demo.user.MemberService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth/scrap")
public class ScrapController {
	@Autowired
	private ScrapService service;
	@Autowired
	private MemberService mservice;
	@Autowired
	private AuctionService aservice;

	@PostMapping("/getbyajax/{auction}")
	public ResponseEntity<Integer> getbyajax(int auction) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		ScrapDto dto = service.getScrapByAuctionAndMember(aservice.get(auction), mservice.getUser(id));
		if (dto == null) {
			return ResponseEntity.ok(1);
		}
		return ResponseEntity.ok(0);
	}

	@PostMapping("/scrap/{auction}")
	public ResponseEntity<Integer> add(int auction) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		ScrapDto dto = service.getScrapByAuctionAndMember(aservice.get(auction), mservice.getUser(id));
		if (dto == null) {
			service.save(new ScrapDto(0, new Member(id), new Auction(auction)));
			return ResponseEntity.ok(1);
		}
		service.del(dto.getNum());
		return ResponseEntity.ok(0);
	}

	@GetMapping("/list")
	public ResponseEntity<ArrayList<ScrapDto>> list() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		return ResponseEntity.ok(service.getScrapByMember(id));
	}
}
