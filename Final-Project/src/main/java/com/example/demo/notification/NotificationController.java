package com.example.demo.notification;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.user.Member;
import com.example.demo.user.MemberDto;
import com.example.demo.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberService memberService;

    @MessageMapping("/notice/{member}")
    public void notice(Notification notification) {
        messagingTemplate.convertAndSend("/sub/notice/"+notification.getName(),notification);
    }

    @MessageMapping("/notice/list/{roomId}")
    public void noticeList(@DestinationVariable String roomId, String sessionId) {
        log.debug("roomId: {}, sessionId={}", roomId,sessionId);
        Object o = redisTemplate.opsForValue().get(roomId);
        String member = "";
        ChatRoom chatRoom = (ChatRoom) o;
        assert chatRoom != null;
        if (chatRoom.getBuyer().equals(sessionId)){
            member = chatRoom.getSeller();
        }else {
            member = chatRoom.getBuyer();
        }
        log.debug("buyer: {}, seller={}", chatRoom.getBuyer(),chatRoom.getSeller());
        log.debug("sessionId: {}", sessionId);
        log.debug("member: {}", member);
        messagingTemplate.convertAndSend("/sub/notice/list/"+member, notificationRepository.findByName(member));
    }
    @GetMapping("/notifications")
    public ResponseEntity<List<Object>> getAllNotifications(@RequestParam String member) {
        Set<Object> notifications = notificationRepository.findByName(member);
        log.debug("notifications: {}", notifications);
        return ResponseEntity.ok(notifications.stream()
                .map(notification -> (Notification) notification)
                .collect(Collectors.toList()));
    }
    @PostMapping("/report/notice")
    public ResponseEntity<Void> noticeAll(Notification notification) {
        log.debug("notification: {}", notification.getContent());
        for (MemberDto member: memberService.getAll()){
            if (member.getType().equals("member")) {
                notification.setName(member.getId());
                notification.setTime();
                notificationRepository.save(notification);
                log.debug("memberId={}",member.getId());
                messagingTemplate.convertAndSend("/sub/notice/list/" + member.getId(), notificationRepository.findByName(member.getId()));
            }
        }
        return ResponseEntity.created(URI.create("/index")).build();
    }

}
