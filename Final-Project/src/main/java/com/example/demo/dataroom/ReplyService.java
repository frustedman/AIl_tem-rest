package com.example.demo.dataroom;

import com.example.demo.report.ReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReplyService {
    @Autowired
    private ReplyDao dao;

    public ReplyDto save(ReplyDto dto){
        Reply r = dao.save(Reply.create(dto));
        return ReplyDto.create(r);
    }

    public ReplyDto get(int num){
        return ReplyDto.create(dao.findById(num).orElse(null));
    }

    public ArrayList<ReplyDto> findAll(DataroomDto dto){
        ArrayList<Reply> l = dao.findAllByDataroomOrderByWdate(Dataroom.create(dto));
        ArrayList<ReplyDto> lDto = new ArrayList<>();
        for(Reply reply : l){
            lDto.add(ReplyDto.create(reply));
        }
        return lDto;
    }
    public void delete(ReplyDto dto){
        dao.delete(Reply.create(dto));
    }
}
