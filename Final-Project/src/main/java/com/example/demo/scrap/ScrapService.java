package com.example.demo.scrap;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.auction.Auction;
import com.example.demo.auction.AuctionDto;
import com.example.demo.user.Member;
import com.example.demo.user.MemberDao;
import com.example.demo.user.MemberDto;

import lombok.extern.slf4j.Slf4j;

@Service
public class ScrapService {
    @Autowired
    private ScrapDao dao;
    @Autowired
    private MemberDao mdao;

    public void save(ScrapDto dto){
        dao.save(Scrap.create(dto));
    }

    public void del(int num){
        dao.deleteById(num);
    }

    public ScrapDto getScrapByAuctionAndMember(AuctionDto auction, MemberDto member){
        Scrap scrap = dao.findByMemberAndAuction(Member.create(member),Auction.create(auction));
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
