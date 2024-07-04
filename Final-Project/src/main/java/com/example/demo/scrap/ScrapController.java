package com.example.demo.scrap;

import com.example.demo.auction.Auction;
import com.example.demo.user.Member;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/auth/scrap")
public class ScrapController {
    @Autowired
    private ScrapService service;

    @ResponseBody
    @PostMapping("/getbyajax")
    public Map getbyajax(int auction, String member) {
        ScrapDto dto = service.getScrapByAuctionAndMember(new Auction(auction),new Member(member));
        Map map = new HashMap();
        if(dto==null) {
            map.put("state", 1);
        }else{
            map.put("state", 0);
        }
        return map;
    }

    @ResponseBody
	@PostMapping("/scrap")
	public Map add(int auction, String member) {
    	System.out.println(member);
        ScrapDto dto = service.getScrapByAuctionAndMember(new Auction(auction),new Member(member));
        System.out.println(dto);
        Map map = new HashMap();
        if(dto==null) {
            service.save(new ScrapDto(0,new Member(member),new Auction(auction) ));
            map.put("state", 1);
        }else{
            log.debug("del실행");
            service.del(dto.getNum());
            map.put("state", 0);
        }
        return map;
    }

    @RequestMapping("/list")
    public String list(String id, ModelMap map) {
        map.addAttribute("list", service.getScrapByMember(id));
        return "scrap/list";
    }
}
