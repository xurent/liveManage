package com.xurent.live.controller;


import com.xurent.live.common.Constants;
import com.xurent.live.model.LiveRoom;
import com.xurent.live.model.in.InputRoom;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.service.LiveRoomService;
import com.xurent.live.service.TokenService;
import com.xurent.live.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequestMapping("/live-room")
@Controller
public class RoomController {


    @Autowired
    private LiveRoomService liveRoomService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TokenService iToken;

    @GetMapping("/getAll")
    @ResponseBody
    public  Object getRooms(@RequestParam(value = "page",defaultValue = "0",required = false)Integer page,
                            @RequestParam(value = "limit",defaultValue ="15",required = false)Integer size){

        System.out.println(page+","+size);

        Page<LiveRoom> rooms= liveRoomService.getAll(page,size);

        System.out.println(rooms.toString());

        return MessageData.ofSuccess("从数据库获取成功",rooms);
    }



    @GetMapping("/get")
    @ResponseBody
    public  Object getRoom(@RequestParam("userId") String userId){


        LiveRoom r=liveRoomService.getRoomByUserName(userId);
        if(r==null){
            return MessageData.ofSuccess("失败",0);
        }
        System.out.println(r);

        return MessageData.ofSuccess("从数据库获取成功",r);
    }



    @GetMapping("/getbykind")
    @ResponseBody
    public  Object getRoomByKind(@RequestParam("kind") Integer kind){

        List<LiveRoom> rooms= (List<LiveRoom>) redisUtil.get(Constants.REDIS_KIND_ROOM);

        if(rooms!=null){
            return MessageData.ofSuccess("从缓存获取成功",rooms);
        }
        List<LiveRoom> newRoom=liveRoomService.getRoomsByKind(kind);
        rooms.addAll(newRoom);
        redisUtil.set(Constants.REDIS_KIND_ROOM,rooms,Constants.ROOMS_KIND_EXPIRE);
        return MessageData.ofSuccess("从数据库获取成功",rooms);
    }


    @PostMapping("/update")
    @ResponseBody
    public  Object UpdateRoom(InputRoom room){

        String name=iToken.getUserInfo().getUsername();
        LiveRoom r= liveRoomService.getRoomByUserName(name);
        if(r!=null){
            r.setAnnouncement(room.getAnnouncement());
            r.setDate(new Date());
            r.setKind(room.getKind());
            r.setRoomImg(room.getRoomImg());
            r.setTitle(room.getTitle());
            liveRoomService.updateRoom(r);
            //从redis设置kind房间过期
            redisUtil.expire(Constants.REDIS_KIND_ROOM,1);

            return MessageData.ofSuccess("从数据库获取成功",0);
        }

        return MessageData.ofError();
    }


    @ResponseBody
    @GetMapping("/open")
    public Object openLive(){

        List<LiveRoom> r= (List<LiveRoom>) redisUtil.get(Constants.REDIS_KIND_ROOM);
       LiveRoom room=liveRoomService.getRoomByUserName(iToken.getUserInfo().getUsername());
       if(room!=null){
           room.setState(1);
           liveRoomService.updateRoom(room);
           if(r!=null){
               int index=r.indexOf(room);
               if(r.get(index)!=null){
                   r.get(index).setState(1);
               }else {
                   r.add(room);
               }
               redisUtil.set(Constants.REDIS_KIND_ROOM,r,Constants.ROOMS_KIND_EXPIRE);
           }

           return MessageData.ofSuccess();
       }

        return MessageData.ofSuccess(101,"请创建直播间",null);
    }


}
