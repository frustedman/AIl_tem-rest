package com.example.demo.auction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bid.BidAddDto;
import com.example.demo.bid.BidDto;
import com.example.demo.bid.BidService;
import com.example.demo.product.ProductService;
import com.example.demo.user.Member;
import com.example.demo.user.MemberDto;
import com.example.demo.user.MemberService;

import lombok.RequiredArgsConstructor;


@CrossOrigin(origins="*")
@RestController
@RequestMapping("/auth/auction")
@RequiredArgsConstructor
public class AuctionController {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private AuctionService aservice;
	@Autowired
	private BidService bservice; // 추가, 수정 / parent로 검색
	@Autowired
	private ProductService pservice;
	@Autowired
	private MemberService mservice;
	
	@PostMapping("/add")
	public ResponseEntity<Boolean> add(AuctionDto a) {
		a.setMax(a.getMin());
		a.setStatus("경매중");
		if(a.getType().equals(Auction.Type.EVENT)) {
			a.setMax(0);
		}
		a.setStart_time(new Date());
		aservice.setTime(a, a.getTime());
		try {
			aservice.save(a);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(false);
		}
		
		return ResponseEntity.ok(true);
	}
	//ggcode
	@MessageMapping("/price")
	@SendTo("/sub/bid")
	public Map send(BidAddDto b) throws InterruptedException {
		Map map=new HashMap();
		int setMax=aservice.bid(b);
		if(setMax>0) {
		map.put("price", setMax);
		}else {
			map.put("msg", "error!");
		}
		return map;
	}
	
	@MessageMapping("/status")
	public void change(int parent) throws InterruptedException {
		Map map=new HashMap();
		AuctionDto auction=aservice.get(parent);
		map.put("parent", parent);
		if(auction.getEnd_time().before(new Date())) {
			auction.setStatus("경매 마감");
			aservice.save(auction);
			map.put("msg", "경매 마감");
		}
		 messagingTemplate.convertAndSend("/sub/bid", map);
	}
	
	
	@GetMapping("/detail")
	public ResponseEntity<AuctionTimeFormatDto> detail(int num) {
		AuctionDto dto=aservice.get(num);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		int point =mservice.getUser(id).getPoint();
		AuctionTimeFormatDto ddto=AuctionTimeFormatDto.create(dto);
		ddto.setPoint(point);
		return ResponseEntity.ok(ddto);
	}

	@GetMapping("/list")
	public ResponseEntity<ArrayList<AuctionDto>> list() {
		return ResponseEntity.ok(aservice.getByStatus("경매중"));
	}

	@GetMapping("/myauction")
	public ResponseEntity<ArrayList<AuctionDto>> myauction() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		return ResponseEntity.ok(aservice.getBySeller(id));
	}

	@GetMapping("/mybidauction")
	public ResponseEntity<ArrayList<BidDto>> mybidauction() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		return ResponseEntity.ok(bservice.getByBuyer2(id));
	}

	@GetMapping("/stop/{num}")
	public ResponseEntity<Boolean>  stop(int num){
		boolean flag=aservice.stopAuction(num);
		if(flag) {
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.internalServerError().body(false);
	}

	@DeleteMapping("/del/{num}")
	public ResponseEntity<Boolean> del(int num) {
		try{
			aservice.delete(num);
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(false);
		}
		return ResponseEntity.ok(true);
	}
	
	
}
