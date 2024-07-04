package com.example.demo.chat.repository;

import com.example.demo.chat.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisChatRoomRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ZSetOperations<String, Object> zSetOperations;
    private final SetOperations<String, Object> setOperations;
    public ChatRoom save(String id,String buyer, String seller,String roomName){
        ChatRoom chatRoom = ChatRoom.createChatRoom(id,buyer,seller, roomName);
        String chatRoomId = chatRoom.getId();
        long timestamp = Instant.now().toEpochMilli();
        log.info("timestamp={}", timestamp);
        log.info("chatRoomId={}", chatRoomId);
        zSetOperations.add("ROOM_"+buyer, chatRoom, timestamp );
        zSetOperations.add("ROOM_"+seller, chatRoom, timestamp );
        redisTemplate.opsForValue().set(chatRoomId, chatRoom);
        Double score = zSetOperations.score("ROOM_"+buyer, chatRoom);
        log.info("score={}", score);
//        redisTemplate.opsForHash().put("CHAT_ROOMS", chatRoom.getId(), chatRoom);
//        redisTemplate.opsForHash().put("CHAT_ROOMS_BY_NAME", name, chatRoom.getId());
        return chatRoom;
    }

    public ChatRoom findRoomByName(String name){
        String roomId = (String) redisTemplate.opsForHash().get("CHAT_ROOMS_BY_NAME", name);
        List<Object> chatRooms = redisTemplate.opsForList().range("CHAT_ROOMS", 0, -1);
        for (Object chatRoom : chatRooms) {

        }
        if (roomId != null) {
            return findRoomById(roomId);
        }
        return null;
    }

    public ChatRoom findRoomById(String id){
        return (ChatRoom) redisTemplate.opsForValue().get(id);
    }

    // 재앙 발생 등급 3
    public void updateChatRoom(String roomId){
        long timestamp = Instant.now().toEpochMilli();
        Object chatRoom = redisTemplate.opsForValue().get(roomId);
        ChatRoom room = (ChatRoom) chatRoom;
        assert room != null;
        String buyer = room.getBuyer();
        String seller = room.getSeller();
        redisTemplate.opsForValue().set(roomId, room);
        zSetOperations.add("ROOM_"+ buyer, chatRoom, timestamp );
        zSetOperations.add("ROOM_"+seller, chatRoom, timestamp );

    }






    public boolean getMen(String roomId){
        //long timestamp = Instant.now().toEpochMilli();
        Object chatRoom = redisTemplate.opsForValue().get(roomId);
        ChatRoom room = (ChatRoom) chatRoom;
        assert room != null;
        return true;
    }



    public Set<Object> findByName(String name){
        return zSetOperations.reverseRange("ROOM_"+name, 0, -1);
    }
}
