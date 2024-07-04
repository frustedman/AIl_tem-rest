package com.example.demo.notification;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class Notification implements Serializable {
    private String name;
    private String title;
    private String content;
    private String time;

    @Builder
    public Notification(String name, String title, String content) {
        this.name = name;
        this.title = title;
        this.content = content;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    public static Notification create(String name, String title, String content) {
        return Notification.builder()
                .name(name)
                .title(title)
                .content(content)
                .build();
    }
    public void setTime(){
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}