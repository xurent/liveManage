package com.xurent.live.controller;


import com.xurent.live.common.Constants;
import com.xurent.live.model.LiveRoom;
import com.xurent.live.model.LiveUrl;
import com.xurent.live.model.in.InputRoom;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.service.LiveRoomService;
import com.xurent.live.service.TokenService;
import com.xurent.live.utils.LiveCodeUtil;
import com.xurent.live.utils.RedisUtil;
import com.xurent.live.utils.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    @Autowired
    private LiveCodeUtil liveCodeUtil;

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
    public  Object getRoomByKind(@RequestParam("kind") String kind){

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
    public  Object UpdateRoom(InputRoom room, @RequestParam(value = "file",required = false) MultipartFile file, HttpServletRequest request) throws IOException {

        String name=iToken.getUserInfo().getUsername();
        LiveRoom r= liveRoomService.getRoomByUserName(name);
        if(r==null){
            r=new LiveRoom();
        }
        System.out.println(file==null);
        if(file!=null){

         String url= UploadFileUtil.getFileUrl(file,name,1,request);
            r.setRoomImg(url);
            System.out.println(url);
        }
            r.setUsername(name);
            r.setAnnouncement(room.getAnnouncement());
            r.setDate(new Date());
            r.setKind(room.getKind());
            r.setTitle(room.getTitle());
            liveRoomService.updateRoom(r);
            //从redis设置kind房间过期
            redisUtil.expire(Constants.REDIS_KIND_ROOM,1);

            return MessageData.ofSuccess("从数据库获取成功",r.getRoomImg());

    }


    @ResponseBody
    @GetMapping("/open")
    public Object openLive(){

        List<LiveRoom> r= (List<LiveRoom>) redisUtil.get(Constants.REDIS_KIND_ROOM);
       LiveRoom room=liveRoomService.getRoomByUserName(iToken.getUserInfo().getUsername());
       if(room!=null){
           SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
           room.setDate(new Date());
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

           LiveUrl urls= (LiveUrl) redisUtil.get(Constants.PLAY+room.getUsername());

           if(urls==null){
               urls=liveCodeUtil.getUrls(room.getUsername(),7);
               redisUtil.set(Constants.PLAY+room.getUsername(),urls,7);
           }

           return MessageData.ofSuccess("直播初始化成功",urls);
       }

        return MessageData.ofSuccess(101,"请创建直播间",null);
    }




    @ResponseBody
    @GetMapping("getLiveUrl")
    public Object getPlayUrl(@RequestParam("uid") String uid){

        LiveUrl urls= (LiveUrl) redisUtil.get(Constants.PLAY+uid);
        if(urls!=null){
            urls.setPushUrl(null);
            return MessageData.ofSuccess("获取成功!",urls);
        }

        return MessageData.ofError("失败");
    }


}
