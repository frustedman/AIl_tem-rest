package com.example.demo.chat.service;

import com.example.demo.auction.Auction;
import com.example.demo.auction.AuctionDao;
import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.repository.RedisChatRoomRepository;
import com.example.demo.chat.repository.RedisMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisChatRoomRepository redisChatRoomRepository;
    private final AuctionDao auctionDao;
    private final RedisMessageRepository messageRepository;
    private final Map<String, List<String>> chatRoomMap = new HashMap<>();
    private final List<String> members = new ArrayList<>();
    public void createChatRoom(String id,String buyer, String seller) {
        if (redisChatRoomRepository.findRoomById(id)==null && !buyer.equals(seller)){
            Optional<Auction> byId = auctionDao.findById(Integer.valueOf(id));
            String name = byId.get().getProduct().getName();
            redisChatRoomRepository.save(id, buyer, seller, name);
            log.debug("productName={}",byId.get().getProduct().getName());
//        return redisChatRoomRepository.save(id,buyer,seller,name);
        }
    }

    //    public List<Object> findAllChatRooms() {
//        return redisChatRoomRepository.findAll();
//    }
    public void updateChatroom(String roomId) {
        log.debug("updateChatroom roomId: {}", roomId);
        redisChatRoomRepository.updateChatRoom(roomId);
    }
    public void discountMen(String roomId,String member) {
        log.debug("updateChatroom roomId: {}", roomId);
        chatRoomMap.get(roomId).remove(member);
    }


    public ChatRoom getChatroom(String roomId) {
        log.debug("updateChatroom roomId: {}", roomId);
        return redisChatRoomRepository.findRoomById(roomId);
    }

    //채팅방 목록 불러오기
    public Set<Object> findByName(String name) {

        Set<Object> set = redisChatRoomRepository.findByName(name);
        // 재앙등급:2

        return redisChatRoomRepository.findByName(name);
    }

    public List<Object> getAllChatMessages(String roomId) {
        //        Set<ChatMessage> chatMessages =new HashSet<>();
//        log.info("messages: {}", messages);
//        if (messages != null) {
//            for (Object message : new ArrayList<>(messages)) { // 안전하게 복사본을 반복합니다
//                if (message instanceof ChatMessage) {
//                    chatMessages.add((ChatMessage) message);
//                }
//            }
//        }
        List<Object> messagesByRoomId = messageRepository.getMessagesByRoomId(roomId);
        return messageRepository.getMessagesByRoomId(roomId);
    }

    public void addChatRoom(String roomId, String member){
        if (!chatRoomMap.containsKey(roomId)){
            List<String> memberList = new ArrayList<>();
            memberList.add(member);
            chatRoomMap.put(roomId, memberList);
            return;
        }
        if(!chatRoomMap.get(roomId).contains(member)) {
            chatRoomMap.get(roomId).add(member);
            log.debug("size={}", chatRoomMap.get(roomId).size());
        }
    }

    public boolean check(String roomId) {
        List<String> strings = chatRoomMap.get(roomId);
        log.info("strings size={}", strings.size());
        log.info("members={}", chatRoomMap.get(roomId).get(0));
        if (strings.size()==2) {
            messageRepository.updateRead(roomId);
            return true;
        }
        return false;
    }
    public void check2(String roomId) {
        log.info("strings size={}", "ㄹㅇㅋㅋ");
        messageRepository.updateRead(roomId);
    }
    public String noticeReceiver(String sender, String roomId){
        String member = null;
        ChatRoom roomById = redisChatRoomRepository.findRoomById(roomId);
        if (roomById.getBuyer().equals(sender)){
            member = roomById.getSeller();
        }else {
            member = roomById.getBuyer();
        }
        return member;
    }
}
