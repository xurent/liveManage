package com.xurent.live.service;

import com.xurent.live.model.LiveRoom;
import com.xurent.live.model.out.OutRoom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LiveRoomService {


    public OutRoom getOutRoomByUserName(String username);

    public LiveRoom getRoomByUserName(String username);

    public List<LiveRoom> getRoomsByKind(String kind);

    public Page<OutRoom>getAll(Integer page, Integer size);

    public void updateRoom(LiveRoom room);

    public void updateOnlineByRid(String rid);

    public void updateSubByRid(String rid);

    public List<OutRoom> getFoucsRoom(String uid);

    public void updateState(String rid,Integer type);
}
