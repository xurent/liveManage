package com.xurent.live.service;

import com.xurent.live.model.LiveRoom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LiveRoomService {


    public LiveRoom getRoomByUserName(String username);

    public List<LiveRoom> getRoomsByKind(Integer kind);

    public Page<LiveRoom>getAll(Integer page,Integer size);

    public void updateRoom(LiveRoom room);



}
