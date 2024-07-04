package com.example.demo.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    private String id;
    private String buyer;
    private String seller;
    private String name;

    @Builder
    public ChatRoom(String id, String buyer, String seller,String name) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.name = name;
    }

    public static ChatRoom createChatRoom(String id,String buyer, String seller, String name) {
        return ChatRoom.builder()
                .id(id)
                .buyer(buyer)
                .seller(seller)
                .name(name)
                .build();
    }
    public static ChatRoom updateChatRoom(String roomId, String buyer, String seller) {
        return ChatRoom.builder()
                .id(roomId)
                .buyer(buyer)
                .seller(seller)
                .build();
    }
}