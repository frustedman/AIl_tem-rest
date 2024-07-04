package com.example.demo.scrap;

import com.example.demo.auction.Auction;
import com.example.demo.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ScrapDao extends JpaRepository<Scrap, Integer> {
    Scrap findByMemberAndAuction(Member member,Auction auction);
    ArrayList<Scrap> findByMember(Member member);
}
