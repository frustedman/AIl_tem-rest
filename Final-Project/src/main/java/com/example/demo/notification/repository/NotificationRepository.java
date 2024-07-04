package com.example.demo.notification.repository;

import com.example.demo.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {

    private final RedisTemplate<String,Object> redisTemplate;

    public void save(Notification notification) {
        long epochMilli = Instant.now().toEpochMilli();
        redisTemplate.opsForZSet().add("NOTIFICATION_"+notification.getName(), notification, epochMilli);
    }

    public Set<Object> findByName(String name) {
        return redisTemplate.opsForZSet().reverseRange("NOTIFICATION_"+name, 0, -1);
    }


}
