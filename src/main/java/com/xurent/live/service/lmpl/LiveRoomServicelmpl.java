package com.xurent.live.service.lmpl;

import com.xurent.live.dao.AnchorDao;
import com.xurent.live.dao.LiveRoomDao;
import com.xurent.live.dao.UserDao;
import com.xurent.live.model.FocusAnchor;
import com.xurent.live.model.LiveRoom;
import com.xurent.live.model.User;
import com.xurent.live.model.out.OutRoom;
import com.xurent.live.service.LiveRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("liveRoomService")
public class LiveRoomServicelmpl implements LiveRoomService {


    @Autowired
    private LiveRoomDao liveRoomDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnchorDao anchorDao;

    @Override
    public OutRoom getOutRoomByUserName(String username) {
        LiveRoom r=liveRoomDao.getByUsername(username);
        if(r==null)return null;
        User u= userDao.getUserByUserName(username);

        return GetRoom(r,u);
    }

    @Override
    public LiveRoom getRoomByUserName(String username) {

      return liveRoomDao.getByUsername(username);
    }

    @Override
    public List<LiveRoom> getRoomsByKind(String kind) {


        return liveRoomDao.findAllByKind(kind);
    }

    @Override
    public Page<OutRoom> getAll(Integer page,Integer size) {
        if(page<1)page=1;
        Pageable pageable=PageRequest.of(page-1,size, Sort.Direction.DESC,"date");
        Page<LiveRoom> pager=liveRoomDao.findAll(pageable);
        List<LiveRoom> lists=pager.getContent();
        Page <OutRoom> data=new PageImpl<OutRoom>(getOut(lists),pageable,pager.getTotalElements());
        return data;
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

    @Override
    public List<OutRoom> getFoucsRoom(String uid) {

       List<FocusAnchor> infos= anchorDao.getAllByUid(uid);

       List<String> ids=new ArrayList<>();
       for(FocusAnchor info:infos){
           System.out.println(info.toString());
           ids.add(info.getAid());
       }
        List<LiveRoom> lists=  liveRoomDao.findAllByUid(ids);

        return getOut(lists);
    }

    @Override
    public void updateState(String rid, Integer type) {
        liveRoomDao.updateState(rid,type);
    }



    private  List<OutRoom> getOut(List<LiveRoom> lists){
        List<OutRoom> outRooms=new ArrayList<OutRoom>();
        for(LiveRoom r:lists){
            User u= userDao.getUserByUserName(r.getUsername());
            outRooms.add(GetRoom(r,u));
        }
        return  outRooms;
    }

    private OutRoom GetRoom(LiveRoom r, User u){

        OutRoom out=new OutRoom();
        out.setOnline(r.getOnline());
        out.setUsername(r.getUsername());
        out.setTitle(r.getTitle());
        out.setRoomImg(r.getRoomImg());
        out.setAnnouncement(r.getAnnouncement());
        out.setKind(r.getKind());
        out.setState(r.getState());
        out.setDate(r.getDate());
        out.setId(r.getId());
        out.setAnchorImg(u.getHeadImg());
        out.setNickname(u.getNickName());
        return out;
    }

}
