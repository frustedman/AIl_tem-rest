package com.example.demo.auction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.demo.auction.Auction.Type;
import com.example.demo.bid.Bid;
import com.example.demo.product.Product;
import com.example.demo.user.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class AuctionDto {
	    private int num;

	 
	    private Member seller;

	    private int min;
	    private int max;

	    private Product product;

	    private String status;

	    private Date start_time;
	    private Date end_time;
	    private Type type;
	    private String content;
	    private String title;
	    private int time;
	    private int bcnt;
	    private Member mino;
	    
	    public static AuctionDto create(Auction a) {
	    	return AuctionDto.builder()
	    			.num(a.getNum())
	    			.seller(a.getSeller())
	    			.min(a.getMin())
	    			.max(a.getMax())
	    			.product(a.getProduct())
	    			.status(a.getStatus())
	    			.start_time(a.getStart_time())
	    			.end_time(a.getEnd_time())
	    			.type(a.getType())
	    			.content(a.getContent())
	    			.title(a.getTitle())
	    			.time(a.getTime())
	    			.bcnt(a.getBcnt())
	    			.mino(a.getMino())
	    			.build();
	    }

	    @Builder
		public AuctionDto(int num, Member seller, int min, int max, Product product, String status, Date start_time, Date end_time,
                          Type type,String content,String title,int time,int bcnt,Member mino) {
			this.num = num;
			this.seller = seller;
			this.min = min;
			this.max = max;
			this.product = product;
			this.status = status;
			this.start_time = start_time;
			this.end_time = end_time;
			this.type = type;
			this.content=content;
			this.title=title;
			this.time=time;
			this.bcnt=bcnt;
			this.mino=mino;
		}
	    
}
