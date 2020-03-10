package com.xurent.live.controller;


import com.xurent.live.common.Constants;
import com.xurent.live.model.FocusAnchor;
import com.xurent.live.model.LiveRoom;
import com.xurent.live.model.LiveUrl;
import com.xurent.live.model.User;
import com.xurent.live.model.in.InputRoom;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.model.out.OutFansInfo;
import com.xurent.live.model.out.OutRoom;
import com.xurent.live.model.out.UserInfo;
import com.xurent.live.service.*;
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

    @Autowired
    private AnchorService anchorService;

    @Autowired
    private UserService userService;

    @Autowired
    private AcountService acountService;


    @GetMapping("/getAll")
    @ResponseBody
    public  Object getRooms(@RequestParam(value = "page",defaultValue = "1",required = false)Integer page,
                            @RequestParam(value = "limit",defaultValue ="15",required = false)Integer size){
        System.out.println(page+","+size);
        Page<OutRoom> rooms= liveRoomService.getAll(page,size);


        return MessageData.ofSuccess("从数据库获取成功",rooms);
    }



    @GetMapping("/get")
    @ResponseBody
    public  Object getRoom(@RequestParam("userId") String userId){


        OutRoom r=liveRoomService.getOutRoomByUserName(userId);
        if(r==null){
            return MessageData.ofSuccess("失败");
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
               redisUtil.set(Constants.PLAY+room.getUsername(),urls,Constants.TOKEN_EXPIRE);
               System.out.println(urls.toString());
           }

           return MessageData.ofSuccess("直播初始化成功",urls);
       }

        return MessageData.ofSuccess(101,"请创建直播间",null);
    }




    @ResponseBody
    @GetMapping("/getLiveUrl")
    public Object getPlayUrl(@RequestParam("uid") String uid){

        LiveUrl urls= (LiveUrl) redisUtil.get(Constants.PLAY+uid);
        if(urls!=null){
            urls.setPushUrl(null);
            return MessageData.ofSuccess("获取成功!",urls);
        }

        return MessageData.ofError("失败");
    }

    /**
     * 关注主播
     * @param aid  主播ID
     * @return
     */

    @ResponseBody
    @GetMapping("/likeAnchor")
    public Object FoucusAnchor(@RequestParam("aid") String aid,@RequestParam("type") Integer type){

        UserInfo u=iToken.getUserInfo();
       User u2= userService.getUserByUserName(aid);

        if((u==null||u2==null)){
            return  MessageData.ofError();
        }

        if(type==0){
            anchorService.UnLikeByUidAndAid(u.getUsername(),aid);
        }else if(type==1){
            FocusAnchor anchor=new FocusAnchor();
            anchor.setAid(aid);
            anchor.setUid(u.getUsername());
            anchor.setDate(new Date());
            anchorService.Like(anchor);

        }
        return  MessageData.ofSuccess();
    }


    @ResponseBody
    @GetMapping("/isFocus")
    public Object getFoucus(@RequestParam("aid") String aid){
        UserInfo info=iToken.getUserInfo();
        if(anchorService.isFoucus(aid,info.getUsername())){

            return MessageData.ofSuccess("已经关注",1);
        }

        return  MessageData.ofSuccess("未关注",0);
    }

    /**
     *
     * @param aid
     * @param type  0 粉丝， 1打赏
     * @return
     */

    @ResponseBody
    @GetMapping("/getTotal")
    public Object getFansAndOther(@RequestParam("aid")String aid,@RequestParam("type") Integer type){

        List<OutFansInfo> info=null;
        if(type==0){

          info=  anchorService.getFansByAid(aid);
        }else if(type==1){

            info=anchorService.getMoneyFans(aid);
        }

        return  MessageData.ofSuccess("成功!",info);
    }


    @ResponseBody
    @GetMapping("/getAnchors")
    public Object getFocusAnchor(){

        String uid=iToken.getUserInfo().getUsername();
        List<OutRoom> rooms=liveRoomService.getFoucsRoom(uid);
        return  MessageData.ofSuccess("成功!",rooms);
    }


    @ResponseBody
    @PostMapping("/giveGift")
    public Object GiveGift(@RequestParam("aid")String aid,@RequestParam("acount") long acount){

        System.out.println(acount);
        String uid=iToken.getUserInfo().getUsername();
       long m= anchorService.GiveAcount(aid,uid,acount);
       if(m>0){

           return  MessageData.ofSuccess("成功!",m);
       }

        return  MessageData.ofError("余额不足!",0);
    }


    @ResponseBody
    @GetMapping("/myAcount")
    public Object getMymoney(){

        long price=acountService.getAcount(iToken.getUserInfo().getUsername());

        return MessageData.ofSuccess("成功",price);

    }


}
