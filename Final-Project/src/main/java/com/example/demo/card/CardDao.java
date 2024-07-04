package com.example.demo.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.card.Card.Type;

import java.util.Date;

@Repository
public interface CardDao extends JpaRepository<Card, String> {
    Card findCardByCardnumAndValidDateAndCvcAndPwdAndType(String cardnum, int validDate, int cvc, int password, Type type);
}
