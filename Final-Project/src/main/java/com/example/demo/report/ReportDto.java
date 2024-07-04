package com.example.demo.report;

import com.example.demo.auction.Auction;
import com.example.demo.user.Member;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ReportDto {
    private int num;

    private Member member;

    private int type;

    private String content;

    private Date wdate;

    private Auction auction;

    private int read;

    public static ReportDto create(Report r){
        return ReportDto.builder()
                .num(r.getNum())
                .member(r.getMember())
                .type(r.getType())
                .content(r.getContent())
                .wdate(r.getWdate())
                .auction(r.getAuction())
                .read(r.getRead())
                .build();
    }

    @Builder
    public ReportDto(int num, Member member, int type, String content, Date wdate, Auction auction, int read) {
        this.num = num;
        this.member = member;
        this.type = type;
        this.content = content;
        this.wdate = wdate;
        this.auction = auction;
        this.read = read;
    }
}
