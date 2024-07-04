package com.example.demo.bid;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.auction.Auction;
import com.example.demo.user.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Bid {

    @Id
    @SequenceGenerator(name = "seq_bid", sequenceName = "seq_bid", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bid")
    private int num;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Auction parent;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member buyer;

    private int price;
    private Date bidtime;
    
    public static Bid create(BidDto dto) {
    	return  Bid.builder()
    			.num(dto.getNum())
    			.parent(dto.getParent())
    			.buyer(dto.getBuyer())
    			.price(dto.getPrice())
    			.bidtime(dto.getBidtime())
    			.build();	
    }

    @Builder
	public Bid(int num, Auction parent, Member buyer, int price,Date bidtime) {
		this.num = num;
		this.parent = parent;
		this.buyer = buyer;
		this.price = price;
		this.bidtime=bidtime;
	}
}
