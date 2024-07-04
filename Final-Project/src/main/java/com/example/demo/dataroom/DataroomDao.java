package com.example.demo.dataroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.ArrayList;

@Repository
public interface DataroomDao extends JpaRepository<Dataroom, Integer> {
    ArrayList<Dataroom> findAllByOrderByWdateDesc();
}
