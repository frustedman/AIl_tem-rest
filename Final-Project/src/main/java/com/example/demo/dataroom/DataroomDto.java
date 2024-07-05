package com.example.demo.dataroom;

import com.example.demo.user.Member;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class DataroomDto {
    private int num;
    private Member writer;
    private String title;
    private String content;
    private int type;
    private Date wdate;


    public static DataroomDto create(Dataroom dto) {
        return DataroomDto.builder()
                .num(dto.getNum())
                .writer(dto.getWriter())
                .title(dto.getTitle())
                .content(dto.getContent())
                .type(dto.getType())
                .wdate(dto.getWdate())
                .build();
    }

    @Builder
    public DataroomDto(int num, Member writer, String title, String content, int type, Date wdate) {
        this.num = num;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.type = type;
        this.wdate = wdate;
    }
}
