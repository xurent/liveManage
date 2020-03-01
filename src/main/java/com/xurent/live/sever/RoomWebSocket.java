package com.xurent.live.sever;


import com.xurent.live.common.Constants;
import com.xurent.live.model.User;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.service.LiveRoomService;
import com.xurent.live.utils.RedisUtil;
import com.xurent.live.utils.WSUtils;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/liveDance/chat/{roomName}/{authcode}")
public class RoomWebSocket {


    /**
     * 0连接成功
     * 1连接失败
     * 10001进入房间
     * 10002退出房间
     * 10003用户文本消息
     * 10004刷礼物
     */

    static Log log=LogFactory.getLog(RoomWebSocket.class);


    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        RoomWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        RoomWebSocket.onlineCount--;
    }

    private static final Map<String, Set<Session>> rooms = new ConcurrentHashMap();




    @OnOpen
    public void Open(@PathParam("roomName") String roomName,@PathParam("authcode") String authcode, Session session) throws IOException {

        if(!CheckId(authcode, session, roomName)){

            return;
        }

        if (!rooms.containsKey(roomName)) {
            // 创建房间不存在时，创建房间
            Set<Session> room = new HashSet<>();
            // 添加用户
            room.add(session);
            rooms.put(roomName, room);
        } else {
            // 房间已存在，直接添加用户到相应的房间
            rooms.get(roomName).add(session);
        }

        User user= (User) WSUtils.redisUtils.get(authcode);

        sendMessage(10001,user.getNickName()+"进入房间",null,rooms.get(roomName),session);
        addOnlineCount();//在线数加1
        log.info("有一连接打开！当前在线人数为" + getOnlineCount());


    }


    @OnMessage
    public void ReceiveMsg(@PathParam("roomName")String roomName,@PathParam("authcode") String authcode,String msg, Session session)throws Exception{
        User user= (User)  WSUtils.redisUtils.get(authcode);
        log.info("收到来自用户"+user.getNickName()+"的信息:"+msg);
        JSONObject object=null;
        try {
            object=JSONObject.fromObject(msg);
        }catch (Exception e){
            session.getBasicRemote().sendText("格式错误!");
        }


        if(object==null)return;
        int code=object.getInt("code");
        switch (code){
            case 10001:
                break;
            case 10002: sendMessage(10002,user.getNickName()+"离开房间",object,rooms.get(roomName),session);
                break;
            case 10003:
                sendMessage(10003,user.getUserName()+"用户发送消息",object,rooms.get(roomName),session);
                break;
            case 10004:
                sendMessage(10004,user.getUserName()+"用户发送礼物",object,rooms.get(roomName),session);
                break;
        }

    }



    @OnClose
    public void disConnect(@PathParam("roomName") String roomName, Session session,@PathParam("authcode") String authcode) {
        Set set= rooms.get(roomName);

        User user= (User)  WSUtils.redisUtils.get("authcode");
        if(user!=null){
            sendMessage(10002,user.getNickName()+"离开房间",null,rooms.get(roomName),session);
        }else {
            set.remove(session);
            return;
        }
        if(!user.getUserName().equals(roomName)&&set!=null){
            if(set.contains(session))
            WSUtils.roomServices.updateSubByRid(roomName);

        }
        set.remove(session);
        log.info(user.getUserName()+user.getNickName()+"退出房间"+roomName+"!");
        subOnlineCount();//人数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());


    }


    @OnError
    public void onError(Session session, Throwable error,@PathParam("roomName")String RoomName,@PathParam("authcode") String authcode) {

        User user= (User)  WSUtils.redisUtils.get(authcode);
        log.error("房间"+RoomName+"的"+user.getUserName()+"用户发生错误");
        if(rooms.get(RoomName)!=null&&rooms.get(RoomName).contains(session)){
            rooms.get(RoomName).remove(session);
            sendMessage(10002,user.getNickName()+"离开房间",null,rooms.get(RoomName),session);
            WSUtils.roomServices.updateSubByRid(RoomName);
        }

        error.printStackTrace();

    }


    private  boolean CheckId(String authcode,Session session,String roomName){

        User user= (User)  WSUtils.redisUtils.get(authcode);
        try {

        if(user==null){

          JSONObject object=JSONObject.fromObject(MessageData.ofError("未登录,非法请求!",null));
          session.getBasicRemote().sendText(object.toString());

         }else{

            JSONObject object=JSONObject.fromObject(MessageData.ofSuccess("连接成功!",null));
          session.getBasicRemote().sendText(object.toString());

            log.info(user.getUserName()+user.getNickName()+"进入房间"+roomName+"!");
            Set set=rooms.get(roomName);
          if(!roomName.equals(user.getUserName())&&set!=null){
              if(!set.contains(session)){
                  WSUtils.roomServices.updateOnlineByRid(roomName);
              }

          }
          return  true;

        }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 群发
     *
     */

    public void sendMessage(int code,String msg,Object data,Set<Session> room,Session session){

           Iterator<Session> it=room.iterator();
           while (it.hasNext()){
               Session user=it.next();

                    if(session.equals(user))continue;

                   try {
                       JSONObject object=JSONObject.fromObject(MessageData.ofSuccess(code,msg,data));
                       user.getAsyncRemote().sendText(object.toString());
                   } catch (Exception e) {
                       e.printStackTrace();
                   }



           }


    }


    public void sendMessage(Object data,Set<Session> room,Session session){

        Iterator<Session> it=room.iterator();
        while (it.hasNext()){
            Session user=it.next();
            if(!user.equals(session)){
                session.getAsyncRemote().sendObject(data);

            }

        }


    }

    // 按照房间名进行广播
    public static void broadcast(String roomName, String msg) throws Exception {
        for (Session session : rooms.get(roomName)) {
            session.getBasicRemote().sendText(msg);
        }
    }



}
