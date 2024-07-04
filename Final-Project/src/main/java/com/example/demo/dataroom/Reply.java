package com.example.demo.dataroom;

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
public class Reply {
    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_reply", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reply")
    private int num;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member writer;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dataroom dataroom;

    private String content;
    private Date wdate;

    @PrePersist
    public void setDate() {
        wdate = new Date();
    }

    public static Reply create(ReplyDto dto) {
        return Reply.builder()
                .num(dto.getNum())
                .writer(dto.getWriter())
                .dataroom(dto.getDataroom())
                .content(dto.getContent())
                .wdate(dto.getWdate())
                .build();
    }

    @Builder
    public Reply(int num, Member writer,Dataroom dataroom, String content,Date wdate) {
        this.num = num;
        this.writer = writer;
        this.dataroom = dataroom;
        this.content = content;
        this.wdate =wdate;
    }
}
