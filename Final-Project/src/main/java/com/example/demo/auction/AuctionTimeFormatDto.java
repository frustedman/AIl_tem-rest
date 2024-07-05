package com.example.demo.auction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.demo.auction.Auction.Type;
import com.example.demo.bid.Bid;
import com.example.demo.product.Product;
import com.example.demo.user.Member;

import jakarta.persistence.PrePersist;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class AuctionTimeFormatDto {
	    private int num;

	 
	    private Member seller;

	    private int min;
	    private int max;

	    private Product product;

	    private String status;

	    private String start_time;
	    private String end_time;
	    private Type type;
	    private String content;
	    private String title;
	    private int bcnt;
	    private Member mino;
	    private int point;
	    
	    public static AuctionTimeFormatDto create(AuctionDto a) {
	    	return AuctionTimeFormatDto.builder()
	    			.num(a.getNum())
	    			.seller(a.getSeller())
	    			.min(a.getMin())
	    			.max(a.getMax())
	    			.product(a.getProduct())
	    			.status(a.getStatus())
	    			.start_time(getTime(a.getStart_time()))
	    			.end_time(getTime(a.getEnd_time()))
	    			.type(a.getType())
	    			.content(a.getContent())
	    			.title(a.getTitle())
	    			.time(a.getTime())
	    			.bcnt(a.getBcnt())
	    			.mino(a.getMino())
	    			.build();
	    }

	    @Builder
		public AuctionTimeFormatDto(int num, Member seller, int min, int max, Product product, String status, String start_time, String end_time,
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
			this.bcnt=bcnt;
			this.mino=mino;
		}
	    public static String getTime (Date time) {
	    	Calendar cal = Calendar.getInstance();
	    	cal.setTime(time);
			String time_set=""+cal.get(Calendar.DAY_OF_MONTH);
			time_set+="일 "+cal.get(Calendar.HOUR_OF_DAY);
			time_set+="시"+cal.get(Calendar.MINUTE)+"분";	
			return time_set;
	    	}
	    	
}
