package com.example.demo.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auction.Auction;
import com.example.demo.auction.AuctionDto;
import com.example.demo.auction.AuctionService;
import com.example.demo.auth.TokenProvider;
import com.example.demo.card.Card;
import com.example.demo.card.CardDto;
import com.example.demo.card.CardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class MemberController {
	@Autowired
	private MemberService service;
	@Autowired
	private CardService cservice;
	@Autowired
	private TokenProvider provider;
	@Autowired
	private AuthenticationManagerBuilder abuilder;

	@PostMapping("/join")
	public ResponseEntity<Boolean> join(MemberDto u) {
		try {
			service.save(u);
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
		return ResponseEntity.ok(true);
	}

	@PostMapping("/login")
	public ResponseEntity<MemberResponseDto> login(String id, String pwd) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, pwd);
		Authentication auth = abuilder.getObject().authenticate(authToken); // authenticate:인증메서드 . 인증한 결과를
																			// Authentication 객체에 담아서 반환
		boolean flag = auth.isAuthenticated(); // 인증결과 true or false
		if (flag) {
			// 인증 성공시 토큰 생성
			String token = provider.getToken(service.getUser(id));
			String type = provider.getUserRole(token);
			// 토큰을 요청자에게 전달
			return ResponseEntity.ok(new MemberResponseDto(id,token,type));  
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@DeleteMapping("/auth/out/{id}")
	public ResponseEntity<ArrayList<MemberDto>> out(@PathVariable String id) {
		try {
			service.delMember(id);
		}catch(Exception e) {
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(service.getAll());
	}

	@GetMapping("/auth/member/list")
	public ResponseEntity<ArrayList<MemberDto>> list() {
		return ResponseEntity.ok(service.getAll());
	}

	@PatchMapping("/auth/member/edit")
	public ResponseEntity<String> edit2(@RequestBody MemberDto m) {
		MemberDto d = service.getUser(m.getId());
		d.setName(m.getName());
		d.setEmail(m.getEmail());
		String msg="";
		if (!m.getPwd().isEmpty()) {
			try {
				service.save(d);
				msg="비밀번호 변경 성공";
			} catch (Exception e) {
				return ResponseEntity.badRequest().body("회원 정보 수정 실패");
			}
			return ResponseEntity.ok(msg);
		}
		try {
			service.edit(d);
			msg="회원 정보 성공";
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("회원 정보 수정 실패");
		}
		return ResponseEntity.ok(msg);
	}

	@PostMapping("/auth/member/card")
	public ResponseEntity<String> card(@RequestBody CardDto dto) {
		// 일치하는 카드 가져오기
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		MemberDto m = service.getUser(id);
		CardDto c = cservice.get(Card.create(dto));
		if (c == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("일치하는 카드가 없습니다");
		}
		m.setCardnum(Card.create(c));
		// 같은카드를 두명이서 등록하면 오류 발생
		try {
			service.edit(m);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 등록된 카드 입니다");
		}
		return ResponseEntity.ok("등록 완료");
	}

	@GetMapping("/auth/member/point")
	public ResponseEntity<MemberDto> pointform() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		MemberDto m = service.getUser(id);
		if (m.getCardnum() == null) {
			return ResponseEntity.badRequest().body(null);
		}
		return ResponseEntity.ok(m);
	}

	@PatchMapping("/auth/member/point")
	public ResponseEntity<MemberDto> point(int point) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String id = auth.getName();
		MemberDto m = service.getUser(id);
		if(m.getCardnum().getPrice()<point) {
			return ResponseEntity.badRequest().body(null);
		}
		m.setPoint(m.getPoint() + point);
		m.setExp(m.getExp() + point);
		if (m.getExp() >= 1400000) {
			m.setRank("Diamond");
		} else if (m.getExp() >= 400000) {
			m.setRank("Gold");
		} else if (m.getExp() >= 100000) {
			m.setRank("Silver");
		}
		try {
			service.edit(m);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
		return ResponseEntity.ok(m);
	}
	@GetMapping("/idcheck")
	public ResponseEntity<Boolean> idcheck(String id) {
		MemberDto u = service.getUser(id);
		if (u == null) {
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.ok(false);
		
	}
}
