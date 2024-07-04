package com.example.demo.auction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.product.Product;
import com.example.demo.user.Member;

@Service
public class AuctionService {

	@Autowired
	private AuctionDao dao;

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

}
