package com.example.demo.scrap;

import com.example.demo.auction.Auction;
import com.example.demo.user.Member;
import com.example.demo.user.MemberDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class ScrapService {
    @Autowired
    private ScrapDao dao;
    @Autowired
    private MemberDao mdao;

    public void save(ScrapDto dto){
        dao.save(Scrap.create(dto));
    }

    public void del(int num){
        log.debug("service; del");
        Scrap scrap = dao.findById(num).orElse(null);
        dao.delete(scrap);
    }

    public ScrapDto getScrapByAuctionAndMember(Auction auction, Member member){
        Scrap scrap = dao.findByMemberAndAuction(member,auction);
        if(scrap == null){
            return null;
        }
        else{
            return ScrapDto.create(scrap);
        }
    }

    public ArrayList<ScrapDto> getScrapByMember(String id){
        Member member = mdao.findById(id).orElse(null);
        ArrayList<Scrap> l = dao.findByMember(member);
        ArrayList<ScrapDto> list = new ArrayList<>();
        for(Scrap scrap : l){
            list.add(ScrapDto.create(scrap));
        }
        return list;
    }
}
