package com.example.demo.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    @Autowired
    private CardDao dao;

    public CardDto get(Card card){
        Card c = dao.findCardByCardnumAndValidDateAndCvcAndPwdAndType(card.getCardnum(),card.getValidDate(),card.getCvc(),card.getPwd(),card.getType());
        if(c == null){
            return null;
        }
        return CardDto.create(c);
    }
}
