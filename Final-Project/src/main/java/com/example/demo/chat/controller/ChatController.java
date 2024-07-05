package com.example.demo.chat.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import com.example.demo.auction.AuctionService;
import com.example.demo.chat.domain.ChatMessage;
import com.example.demo.chat.domain.ChatMessagePublisher;
import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.dto.ChatRoomDto;
import com.example.demo.chat.repository.RedisMessageRepository;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.notification.Notification;
import com.example.demo.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/auth/rooms")
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatMessagePublisher chatMessagePublisher;
    private final ChatRoomService chatRoomService;
    private final RedisMessageRepository redisMessageRepository;
    private final AuctionService auctionService;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/load")
    public ResponseEntity<ChatRoomDto> getChatRooms(String name, String roomId) {
        Set<Object> byName = chatRoomService.findByName(name);
        int unreadMessagesByRoomId = redisMessageRepository.getUnreadMessagesByRoomId(roomId, name);
        return ResponseEntity.ok(ChatRoomDto.create(byName, unreadMessagesByRoomId));
    }
    @GetMapping(params = {"id","seller", "buyer"})
    public ResponseEntity<String> rooms(@RequestParam String id,@RequestParam String seller,@RequestParam String buyer) {
        chatRoomService.createChatRoom(id,buyer,seller);
        return ResponseEntity.ok(seller);
    }

    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createRoom(String id,String buyer, String seller) {
        chatRoomService.createChatRoom(id,buyer, seller);
        return ResponseEntity.ok(chatRoomService.getChatroom(id));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Object>> getChatMessages(@PathVariable String roomId) {
        return ResponseEntity.ok(chatRoomService.getAllChatMessages(roomId));
    }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/sub/messages/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, @Payload ChatMessage message) {
        message.setRoomId(roomId);
        message.updateTimestamp();

        boolean check = chatRoomService.check(roomId);
        if (check){
            message.setRead(true);
        }else {
            String s = chatRoomService.noticeReceiver(message.getSender(), message.getRoomId());
            Notification notification = Notification.create(s, chatRoomService.getChatroom(roomId).getName(), message.getContent());
            notificationRepository.save(notification);
            simpMessagingTemplate.convertAndSend("/sub/notice/list/"+s, notificationRepository.findByName(s));
        }
        chatRoomService.updateChatroom(roomId);
//        if(chatRoomService.getChatroom(roomId)) {
//        	message.setRead(true);
//        }
        chatMessagePublisher.publish(message);
        return message;
    }


    //d
    @MessageMapping("/chat/image/{roomId}")
    @SendTo("/sub/messages/{roomId}")
    public ChatMessage handleImageUpload(@DestinationVariable String roomId, @Payload ChatMessage message) {
        message.setRoomId(roomId);
        message.updateTimestamp();
        boolean check = chatRoomService.check(roomId);
        if (check){
            message.setRead(true);
        }
        chatRoomService.updateChatroom(roomId);
        chatMessagePublisher.publish(message);
        return message;
    }

    @MessageMapping("/chat/rooms/{roomId}")
    @SendTo("/sub/rooms")
    public String handleRooms(@DestinationVariable String roomId) {
        chatRoomService.updateChatroom(roomId);
        log.info("message={}", "ok");
        return "OK";
    }

    @GetMapping("/lastMessage")
    public ResponseEntity<ChatMessage> lastMessage(String roomId) {
        Set<Object> lastMessage = redisMessageRepository.getLastMessage(roomId);
        for (Object o : lastMessage) {
            ChatMessage chatMessage = (ChatMessage) o;
            if (chatMessage.getContent().length()>8){
                chatMessage.setContent(chatMessage.getContent().substring(0,8)+"...");
            }
            return ResponseEntity.ok(chatMessage);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/enter/{roomId}")
    public ResponseEntity<ChatRoom> enter(@PathVariable String roomId, @RequestParam String member) {
    	ChatRoom chatRoom = chatRoomService.enterRoom(roomId, member);
//        map.put("seller", auctionService.get(Integer.parseInt(roomId)).getSeller());
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/out/{roomId}")
    public ResponseEntity<String> out(@PathVariable String roomId, @RequestParam String member) {
        chatRoomService.discountMen(roomId, member);
        return ResponseEntity.ok(member+"님이 퇴장하셨습니다.");
    }

//    @MessageMapping("/enter/{roomId}")
//    @SendTo("/sub/enter/{roomId}")
//    public String reload(@DestinationVariable String roomId) {
//        return "reload";
//    }

    @GetMapping("/unread/{roomId}")
    public ResponseEntity<Integer> unread(@RequestParam String roomId, @RequestParam String member) {
        chatRoomService.getChatroom(roomId);
        int unreadMessagesByRoomId = redisMessageRepository.getUnreadMessagesByRoomId(roomId, member);
        return ResponseEntity.ok(unreadMessagesByRoomId);
        
    }
}
