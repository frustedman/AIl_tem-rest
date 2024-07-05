package com.example.demo.auction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.example.demo.bid.Bid;
import com.example.demo.bid.BidAddDto;
import com.example.demo.bid.BidDao;
import com.example.demo.bid.BidDto;
import com.example.demo.product.Product;
import com.example.demo.user.Member;
import com.example.demo.user.MemberDao;
import com.example.demo.user.MemberDto;

@Service
public class AuctionService {

	@Autowired
	private AuctionDao dao;
	@Autowired
	private BidDao bdao;
	@Autowired
	private MemberDao mdao;

	public void save(AuctionDto dto) {
		dao.save(Auction.create(dto));
	}

	// end 타임 설정
	public void setTime(AuctionDto dto, int t) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dto.getStart_time());
		int time = cal.get(Calendar.MINUTE);
		cal.set(Calendar.MINUTE, t + time);
//		int time = cal.get(Calendar.HOUR);
//		cal.set(Calendar.HOUR, t + time);
		dto.setEnd_time(cal.getTime());
	}

	// 번호로 삭제
	public void delete(int num) {
		dao.deleteById(num);
	}

	// 번호로 찾기
	public AuctionDto get(int num) {
		Auction a = dao.findById(num).orElse(null);
		if (a == null) {
			return null;
		}
		return AuctionDto.create(a);
	}

	// 전체목록
	public ArrayList<AuctionDto> getAll() {
		List<Auction> l = dao.findAllByOrderByNumDesc();
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 전체목록
		public ArrayList<AuctionDto> getAllByBids(String status) {
			List<Auction> l = dao.findByStatusOrderByBcntDesc(status);
			ArrayList<AuctionDto> list = new ArrayList<>();
			for (Auction a : l) {
				list.add(AuctionDto.create(a));
			}
			return list;

		}

	
	
	// 판매자로 찾기
	public ArrayList<AuctionDto> getBySeller(String seller) {
		List<Auction> l = dao.findBySellerOrderByNumDesc(new Member(seller, "", "", "", null, 0, "", 0, ""));
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 상품으로 찾기
	public ArrayList<AuctionDto> getByProduct(Product product) {
		List<Auction> l = dao.findByProduct(product);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 타입으로 찾기
	public ArrayList<AuctionDto> getByType(Auction.Type type) {
		List<Auction> l = dao.findByType(type);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 상태로 찾기
	public ArrayList<AuctionDto> getByStatus(String status) {
		List<Auction> l = dao.findByStatusOrderByNumDesc(status);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 상품명으로 찾기
	public ArrayList<AuctionDto> getByProdName(String Name) {
		List<Auction> l = dao.findByProductNameLike(Name);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}

	// 카테고리로 찾기
	public ArrayList<AuctionDto> getByProdCategories(Product.Categories categories) {
		List<Auction> l = dao.findByProductCategories(categories);
		ArrayList<AuctionDto> list = new ArrayList<>();
		for (Auction a : l) {
			list.add(AuctionDto.create(a));
		}
		return list;

	}
	
	public boolean stopAuction(int num ) {
		Auction a= dao.findById(num).orElse(null);
		AuctionDto auction=AuctionDto.create(a);
		if(a==null) {
			return false;
		}
		auction.setStatus("경매마감");
		switch(auction.getType()) {
		case BLIND :{
			ArrayList<Bid> blist =bdao.findByParentOrderByNum(null);
			for(Bid bid :blist) {
				Member buyer=bid.getBuyer();
				buyer.setPoint(buyer.getPoint()+bid.getPrice());
				try {
					mdao.save(buyer);
				}catch(Exception e){
					return false;
				}
			}
		}
		case NORMAL:{
			ArrayList<Bid> blist =bdao.findByParentOrderByNum(null);
			Member buyer=blist.get(0).getBuyer();
			buyer.setPoint(buyer.getPoint()+blist.get(0).getPrice());
			try {
				mdao.save(buyer);
			}catch(Exception e) {
				return false;
			}
		}
		case EVENT:{
			ArrayList<Bid> blist =bdao.findByParentOrderByNum(null);
			for(Bid bid :blist) {
				Member buyer=bid.getBuyer();
				buyer.setPoint(buyer.getPoint()+bid.getPrice());
				try{
					mdao.save(buyer);
				}catch(Exception e) {
					return false;
				}
			}
		}
		}
		return true;
	}
	public int bid(BidAddDto b){
		Member buyer= mdao.findById(b.getBuyer()).orElse(null);
		Auction auction=dao.findById(b.getParent()).orElse(null);
		AuctionDto adto=AuctionDto.create(auction);
		BidDto dto=new BidDto(b.getNum(),auction,buyer,b.getPrice(),new Date());
		if(dto.getBidtime().after(auction.getEnd_time())) {
			return 0;
		}
		if(!(auction.getType().equals(Auction.Type.EVENT))) {
			if(bdao.findByParentOrderByNum(auction).size()>0 && !(auction.getType().equals(Auction.Type.BLIND))) {
				Bid pbid=bdao.findById(auction.getNum()).orElse(null);
				int getPoint=pbid.getPrice();
				Member pbuyer= mdao.findById(pbid.getBuyer().getId()).orElse(null);
				pbuyer.setPoint(pbuyer.getPoint()+getPoint);
				mdao.save(pbuyer);
			}
		}
		buyer.setPoint(buyer.getPoint()-b.getPrice());
		bdao.save(Bid.create(dto));
		adto.setBcnt(auction.getBcnt()+1);
		if((auction.getType().equals(Auction.Type.EVENT))) {
			adto.setMax(auction.getMax()+b.getPrice());
		}else {
			adto.setMax(b.getPrice());
		}
		save(adto);
		mdao.save(buyer);
		return adto.getMax();
	}

}
