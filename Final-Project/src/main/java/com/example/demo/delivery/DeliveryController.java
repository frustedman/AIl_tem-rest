package com.example.demo.delivery;

import com.example.demo.auction.Auction;
import com.example.demo.auction.AuctionService;
import com.example.demo.chat.domain.ChatMessage;
import com.example.demo.chat.repository.RedisMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/auth/delivery")
public class DeliveryController {
    @Autowired
    private DeliveryService service;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private RedisMessageRepository repository;

    @GetMapping("/add")
    public String addForm() {
        return "delivery/add";
    }

    @ResponseBody
    @PostMapping("/add")
    public Map add(String t_invoice, String t_code, String t_key, String auction){
        log.debug("t_invoice: {}", t_invoice);
        log.debug("t_code: {}", t_code);
        log.debug("t_key: {}", t_key);
        log.debug("auction={}",auction);
        ChatMessage message = new ChatMessage("UUID.randomUUID().toString()", auctionService.get(Integer.parseInt(auction)).getSeller().getId(),"notice","📢운송장 등록되었습니다.", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),auction,true);
        repository.saveMessage(message);
        simpMessagingTemplate.convertAndSend("/sub/messages/"+ auction, message);
        DeliveryDto dto = new DeliveryDto(t_invoice,t_code,t_key,new Auction(Integer.parseInt(auction)));
        service.save(dto);
        Map map = new HashMap<>();
        return map;
    }

    @ResponseBody
    @PostMapping("/check")
    public Map check(String auction){
        Map map = new HashMap<>();
        log.debug("auction={}",auction);
        DeliveryDto dto = service.findByAuction(auction);
        map.put("dto",dto);
        log.debug("dto={}",dto);
        return map;
    }
    @ResponseBody
    @PostMapping("/addajax")
    public Map addAjax(String auction){
        DeliveryDto dto = service.findByAuction(auction);
        Map map = new HashMap<>();
        if(dto==null){
            map.put("flag", true);
        }
        else{
            map.put("flag", false);
        }
        return map;
    }
}
