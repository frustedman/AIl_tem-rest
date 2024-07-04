package com.example.demo.dataroom;

import com.example.demo.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ReplyDto {
    private int num;

    private Member writer;

    private Dataroom dataroom;

    private String content;
    private Date wdate;


    public static ReplyDto create(Reply dto) {
        return ReplyDto.builder()
                .num(dto.getNum())
                .writer(dto.getWriter())
                .dataroom(dto.getDataroom())
                .content(dto.getContent())
                .wdate(dto.getWdate())
                .build();
    }

    @Builder
    public ReplyDto(int num, Member writer,Dataroom dataroom, String content,Date wdate) {
        this.num = num;
        this.writer = writer;
        this.dataroom = dataroom;
        this.content = content;
        this.wdate =wdate;
    }
}
