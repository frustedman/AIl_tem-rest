package com.example.demo.chat.domain;

import com.example.demo.chat.repository.RedisMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessagePublisher {

    private final ChannelTopic topic;
    private final RedisMessageRepository messageRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChatMessage message) {
        log.info("message: {}", message.getContent());
                messageRepository.saveMessage(message);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
