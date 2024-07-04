package com.example.demo.chat.repository;

import com.example.demo.chat.domain.ChatMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class RedisMessageRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ZSetOperations<String, Object> zSetOperations;

    public void saveMessage(ChatMessage message) {
        long timestamp = Instant.now().toEpochMilli();
//        redisTemplate.opsForList().rightPush("CHAT_MESSAGES_" + message.getRoomId(), message);
        message.setId(UUID.randomUUID().toString());
        String messageKey = "CHAT_MESSAGE_" + message.getRoomId() + "_" + message.getId();
        redisTemplate.opsForValue().set(messageKey, message);
        zSetOperations.add("CHAT_MESSAGE_" + message.getRoomId(), messageKey, timestamp);
//        zSetOperations.add("CHAT_MESSAGE_" + message.getRoomId(), message, timestamp);
    }
    public List<Object> getMessagesByRoomId(String roomId) {
//        return redisTemplate.opsForList().range("CHAT_MESSAGES_" + roomId, 0, -1);
        Set<Object> range = zSetOperations.range("CHAT_MESSAGE_" + roomId, 0, -1);
        assert range != null;
        return range.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .collect(Collectors.toList());
    }

    public int getUnreadMessagesByRoomId(String roomId, String member) {
        List<Object> messagesByRoomId = getMessagesByRoomId(roomId);
        int cnt = 0;
        for (Object msg : messagesByRoomId) {
            ChatMessage chatMessage = (ChatMessage) msg;
            if (!chatMessage.isRead() && !member.equals(chatMessage.getSender())){
                cnt += 1;
            }
        }
        return cnt;
    }

    public Set<Object> getLastMessage(String roomId) {
        Set<Object> range = zSetOperations.range("CHAT_MESSAGE_" + roomId, -1, -1);
        log.debug("roomId in messageRepository={}", roomId);
        log.debug("range: {}", range);
        assert range != null;
        return range.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .collect(Collectors.toSet());
    }

    public void updateRead(String roomId) {
        List<Object> messagesByRoomId = getMessagesByRoomId(roomId);
        for (Object message : messagesByRoomId) {
            ChatMessage chatMessage = (ChatMessage) message;
            chatMessage.setRead(true);
            String messageKey = "CHAT_MESSAGE_" + roomId + "_" + chatMessage.getId();
            redisTemplate.opsForValue().set(messageKey, chatMessage);
        }

    }
}
