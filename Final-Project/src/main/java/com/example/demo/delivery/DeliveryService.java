package com.example.demo.delivery;

import com.example.demo.auction.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    @Autowired
    private DeliveryDao dao;

    public void save(DeliveryDto dto) {
        dao.save(Delivery.create(dto));
    }

    public void delete(DeliveryDto dto) {
        dao.delete(Delivery.create(dto));
    }

    public DeliveryDto findByAuction(String id) {
        Delivery d =  dao.findByAuction(new Auction(Integer.parseInt(id)));
        if(d == null) {
            return null;
        }
        return DeliveryDto.create(d);
    }
}
