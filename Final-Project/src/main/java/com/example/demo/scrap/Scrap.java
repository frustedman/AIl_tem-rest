package com.example.demo.scrap;

import com.example.demo.auction.Auction;
import com.example.demo.product.Product;
import com.example.demo.user.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Scrap {
    @Id
    @SequenceGenerator(name = "seq_scrap", sequenceName = "seq_scrap", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_scrap")
    private int num;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Auction auction;

    public static Scrap create(ScrapDto dto){
        return Scrap.builder()
                .num(dto.getNum())
                .member(dto.getMember())
                .auction(dto.getAuction())
                .build();
    }

    @Builder
    public Scrap(int num, Member member, Auction auction){
        this.num = num;
        this.member = member;
        this.auction = auction;
    }
}
