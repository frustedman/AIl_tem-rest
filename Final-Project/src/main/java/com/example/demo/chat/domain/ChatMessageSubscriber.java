package com.example.demo.chat.domain;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageSubscriber implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(ChatMessageSubscriber.class);
    private final SimpMessagingTemplate template;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.debug("onMessage: {}", message);
        String messageBody = new String(message.getBody());
        template.convertAndSend("/topic/messages", messageBody);
    }
}
