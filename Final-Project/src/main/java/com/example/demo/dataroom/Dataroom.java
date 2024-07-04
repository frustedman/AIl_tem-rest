package com.example.demo.dataroom;

import com.example.demo.auction.Auction;
import com.example.demo.auction.AuctionDto;
import com.example.demo.user.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Dataroom {
    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_data", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_data")
    private int num;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member writer;


    private String title;
    private String content;
    private int type;
    private Date wdate;

    @PrePersist
    public void setDate() {
        wdate = new Date();
    }

    public static Dataroom create(DataroomDto dto) {
        return Dataroom.builder()
                .num(dto.getNum())
                .writer(dto.getWriter())
                .title(dto.getTitle())
                .content(dto.getContent())
                .type(dto.getType())
                .wdate(dto.getWdate())
                .build();
    }

    @Builder
    public Dataroom(int num, Member writer, String title, String content, int type, Date wdate) {
        this.num = num;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.type = type;
        this.wdate =wdate;
    }
}
