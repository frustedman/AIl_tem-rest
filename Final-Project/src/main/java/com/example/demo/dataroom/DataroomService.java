package com.example.demo.dataroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataroomService {
    @Autowired
    private DataroomDao dao;

    public void save(DataroomDto dto){
        dao.save(Dataroom.create(dto));
    }

    public DataroomDto get(int num){
        return DataroomDto.create(dao.findById(num).orElse(null));
    }

    public void del(DataroomDto dto){
        dao.delete(Dataroom.create(dto));
    }
    public ArrayList<DataroomDto> findAll(){
        ArrayList<Dataroom> l = dao.findAllByOrderByWdateDesc();
        ArrayList<DataroomDto> list = new ArrayList<>();
        for(Dataroom d : l){
            list.add(DataroomDto.create(d));
        }
        return list;
    }
}
