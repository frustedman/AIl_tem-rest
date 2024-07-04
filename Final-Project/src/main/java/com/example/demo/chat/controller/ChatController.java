package com.example.demo.chat.controller;

import com.example.demo.auction.AuctionService;
import com.example.demo.chat.domain.ChatMessage;
import com.example.demo.chat.domain.ChatMessagePublisher;
import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.repository.RedisMessageRepository;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.notification.Notification;
import com.example.demo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
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
    @ResponseBody
    public Map<String,Object> getChatRooms(String name, String roomId) {
//        log.info("rooms={}", chatRoomService.findAllChatRooms());
//        return chatRoomService.findAllChatRooms();
        log.info("name: {}", name);
        log.info("setobject={}", chatRoomService.findByName(name));
        Set<Object> byName = chatRoomService.findByName(name);
        log.info("size={}", byName.size());
        Map<String,Object> map = new HashMap<>();
        int unreadMessagesByRoomId = redisMessageRepository.getUnreadMessagesByRoomId(roomId, name);
        map.put("byName",byName);
        map.put("count",unreadMessagesByRoomId);
        return map;
    }
    @GetMapping(params = {"id","seller", "buyer"})
    public String rooms(@RequestParam String id,@RequestParam String seller,@RequestParam String buyer, Model model) {
        chatRoomService.createChatRoom(id,buyer,seller);
//        if (byName != null) {
//            seller="nope!!";
//        }
        log.info("seller={}", seller);
        model.addAttribute("seller", seller);
        return "chat/rooms";
    }

    @RequestMapping
    public String rooms() {
        return "chat/rooms";
    }

    @PostMapping("/create")
    @ResponseBody
    public ChatRoom createRoom(String id,String buyer, String seller) {
        log.info("buyer={}", buyer);
        log.info("seller={}", seller);
        chatRoomService.createChatRoom(id,buyer, seller);
        return chatRoomService.getChatroom(id);
    }

    @GetMapping("/{roomId}/messages")
    @ResponseBody
    public List<Object> getChatMessages(@PathVariable String roomId) {
        log.info("roomId={}", roomId);
        // 상대방이 들어왔을 트루로
        return chatRoomService.getAllChatMessages(roomId);
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
    @ResponseBody
    public Map<String,Object> lastMessage(String roomId) {
        log.debug("roomId={}", roomId);
        Set<Object> lastMessage = redisMessageRepository.getLastMessage(roomId);
        for (Object o : lastMessage) {
            ChatMessage chatMessage = (ChatMessage) o;
            if (chatMessage.getContent().length()>8){
                chatMessage.setContent(chatMessage.getContent().substring(0,8)+"...");
            }
        }
        log.info("lastMessage={}", lastMessage);
        Map<String, Object> content = new HashMap<>();
        content.put("content",lastMessage);
        return content;
    }

    @GetMapping("/enter/{roomId}")
    @ResponseBody
    public Map<Object, Object> enter(@PathVariable String roomId, @RequestParam String member) {
        Map<Object, Object> map = new HashMap<>();
        chatRoomService.addChatRoom(roomId,member);
        chatRoomService.check(roomId);
        Set<Object> lastMessage = redisMessageRepository.getLastMessage(roomId);
        for(Object o:lastMessage) {
            ChatMessage message=(ChatMessage)o;
            log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@={}", message.getSender().equals(member));
            if(!(message.getSender().equals(member))) {
                log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&={}", message.getSender().equals(member));
                chatRoomService.check2(roomId);
            }
        }
        log.debug("roomIddd={}", roomId);
        log.debug("chatRoomName={}",chatRoomService.getChatroom(roomId));
        map.put("message", chatRoomService.getChatroom(roomId));
        map.put("seller", auctionService.get(Integer.parseInt(roomId)).getSeller());
        return map;
    }

    @GetMapping("/out/{roomId}")
    @ResponseBody
    public Map<String, String> out(@PathVariable String roomId, @RequestParam String member) {
        Map<String, String> map = new HashMap<>();
        chatRoomService.discountMen(roomId, member);
        map.put("msg", member+" out!");
        return map;
    }



    @MessageMapping("/enter/{roomId}")
    @SendTo("/sub/enter/{roomId}")
    public String reload(@DestinationVariable String roomId) {
        return "reload";
    }

    @GetMapping("/unread/{roomId}")
    @ResponseBody
    public Map<Object, Object> unread(@RequestParam String roomId, @RequestParam String member) {
        Map<Object, Object> map = new HashMap<>();
        chatRoomService.getChatroom(roomId);
        int unreadMessagesByRoomId = redisMessageRepository.getUnreadMessagesByRoomId(roomId, member);
        map.put("unreadMessagesByRoomId",unreadMessagesByRoomId);
        return map;
    }
}
