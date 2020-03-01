package com.xurent.live.dao;

import com.xurent.live.model.LiveRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LiveRoomDao extends JpaRepository<LiveRoom,Integer> {


    LiveRoom getByUsername(String username);


    List<LiveRoom> findAllByKind(String kind);


    @Modifying
    @Query("update  LiveRoom r set  r.online=r.online+1 where  r.username=?1")
    void updateOnlineByRoomId(String rid);

    @Modifying
    @Query("update  LiveRoom r set  r.online=r.online-1 where  r.username=?1 and r.online>0")
    void updateSubByRoomId(String rid);
}
