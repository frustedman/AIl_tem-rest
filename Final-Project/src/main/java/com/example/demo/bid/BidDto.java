package com.example.demo.bid;

import java.util.Date;

import com.example.demo.auction.Auction;
import com.example.demo.user.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BidDto {

	    private int num;
	    private Auction parent;
	    private Member buyer;
	    private int price;
	    private Date bidtime;
	   
	    
	    public static BidDto create(Bid b) {
	    	return BidDto.builder()
	    			.num(b.getNum())
	    			.parent(b.getParent())
	    			.buyer(b.getBuyer())
	    			.price(b.getPrice())
	    			.build();
	    }

	    @Builder
		public BidDto(int num, Auction parent, Member buyer, int price,Date bidtime) {
			this.num = num;
			this.parent = parent;
			this.buyer = buyer;
			this.price = price;
			this.bidtime=bidtime;
		}
}
