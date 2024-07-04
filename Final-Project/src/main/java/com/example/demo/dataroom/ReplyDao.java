package com.example.demo.dataroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ReplyDao extends JpaRepository<Reply, Integer> {
    ArrayList<Reply> findAllByDataroomOrderByWdate(Dataroom dataroom);
}
