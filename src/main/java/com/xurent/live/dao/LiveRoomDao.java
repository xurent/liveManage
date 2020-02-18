package com.xurent.live.dao;

import com.xurent.live.model.LiveRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LiveRoomDao extends JpaRepository<LiveRoom,Integer> {


    LiveRoom getByUsername(String username);


    List<LiveRoom> findAllByKind(Integer kind);



}
