package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.auction.Auction;
import com.example.demo.auction.AuctionDao;
import com.example.demo.bid.Bid;
import com.example.demo.bid.BidDao;
import com.example.demo.bid.BidDto;
import com.example.demo.product.ProductDao;
@SpringBootTest
class FinalProjectApplicationTests {

	@Autowired
	private BidDao dao;
	
	
//	@Test
//	void 비드빌더테스트() {
//		Bid b=dao.save(Bid.create(new BidDto(0,200)));
//		Bid b2=dao.save(Bid.create(new BidDto(100,200)));
//		assertEquals(1, b.getNum());
//		assertEquals(2, b2.getNum());
//		assertEquals(200, b.getPrice());
//	}

}
