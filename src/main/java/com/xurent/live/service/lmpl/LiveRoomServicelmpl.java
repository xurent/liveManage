package com.xurent.live.service.lmpl;

import com.xurent.live.dao.LiveRoomDao;
import com.xurent.live.model.LiveRoom;
import com.xurent.live.service.LiveRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("liveRoomService")
public class LiveRoomServicelmpl implements LiveRoomService {


    @Autowired
    private LiveRoomDao liveRoomDao;

    @Override
    public LiveRoom getRoomByUserName(String username) {

        return liveRoomDao.getByUsername(username);
    }

    @Override
    public List<LiveRoom> getRoomsByKind(String kind) {


        return liveRoomDao.findAllByKind(kind);
    }

    @Override
    public Page<LiveRoom> getAll(Integer page, Integer size) {
        if(page<1)page=1;

        Pageable pageable= PageRequest.of(page-1, size, Sort.Direction.DESC,"date");
        return liveRoomDao.findAll(pageable);
    }

    @Override
    public void updateRoom(LiveRoom room) {
        liveRoomDao.saveAndFlush(room);
    }

    @Override
    public void updateOnlineByRid( String rid) {

        liveRoomDao.updateOnlineByRoomId(rid);

    }

    @Override
    public void updateSubByRid(String rid) {
        liveRoomDao.updateSubByRoomId(rid);
    }
}
