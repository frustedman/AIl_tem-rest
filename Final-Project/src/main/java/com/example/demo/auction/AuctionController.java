package com.example.demo.auction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.TokenProvider;
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
	private AuctionService aservice;
	@Autowired
	private BidService bservice; // 추가, 수정 / parent로 검색
	@Autowired
	private ProductService pservice;
	@Autowired
	private MemberService mservice;
	@Autowired
	private TokenProvider provider;
	@Autowired
	private AuthenticationManagerBuilder abuilder;
	
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
	
	@MessageMapping("/price")
	@SendTo("/sub/bid")
	public Map send(BidAddDto b) throws InterruptedException {
		Map map=new HashMap();
		MemberDto buyer= mservice.getUser(b.getBuyer());
		AuctionDto auction=aservice.get(b.getParent());
		map.put("parent", b.getParent());
		BidDto dto=new BidDto(b.getNum(),Auction.create(auction),Member.create(buyer),b.getPrice(),new Date());
		if(dto.getBidtime().after(auction.getEnd_time())) {
			map.put("msg","end");
			return map;
		}
		if(!(auction.getType().equals(Auction.Type.EVENT))) {
			if(bservice.getByParent(b.getParent()).size()>0 && !(auction.getType().equals(Auction.Type.BLIND))) {
				BidDto pbid=bservice.getByBuyer(auction.getNum());
				int getPoint=pbid.getPrice();
				System.out.println(getPoint);
				MemberDto pbuyer= mservice.getUser(pbid.getBuyer().getId());
				System.out.println(pbuyer.getId());
				pbuyer.setPoint(pbuyer.getPoint()+getPoint);
				System.out.println(pbuyer.getPoint());
				mservice.edit(pbuyer);
			}
		}
		buyer.setPoint(buyer.getPoint()-b.getPrice());
		bservice.save(dto);
		auction.setBcnt(auction.getBcnt()+1);
		if((auction.getType().equals(Auction.Type.EVENT))) {
			System.out.println(b.getPrice());
			auction.setMax(auction.getMax()+b.getPrice());
		}else {
			auction.setMax(b.getPrice());
		}
		System.out.println(3);
		aservice.save(auction);
		mservice.edit(buyer);
		String price=""+b.getPrice();
		map.put("price", auction.getMax());
		return map;
	}
	
	@MessageMapping("/status")
	@SendTo("/sub/bid")
	public Map change(int parent) throws InterruptedException {
		Map map=new HashMap();
		AuctionDto auction=aservice.get(parent);
		map.put("parent", parent);
		if(auction.getEnd_time().before(new Date())) {
			auction.setStatus("경매 마감");
			aservice.save(auction);
			map.put("msg", "경매 마감");
		}
		return map;
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
		AuctionDto auction=aservice.get(num);
		try {
			auction.setStatus("경매 마감");
		}catch(Exception e) {
			return ResponseEntity.ok(false);
		}
		return ResponseEntity.ok(true);
	}

	@DeleteMapping("/del/{num}")
	public String del(int num) {
		aservice.delete(num);
		map.addAttri
		return "auction/adminlist";
	}
	
	
}
