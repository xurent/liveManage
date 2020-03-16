package com.xurent.live.sever;


import com.xurent.live.common.Constants;
import com.xurent.live.model.User;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.model.out.OutFansInfo;
import com.xurent.live.model.out.SysNotice;
import com.xurent.live.utils.WSUtils;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/system/{authcode}")
public class SystemSocket {



    static Log log=LogFactory.getLog(SystemSocket.class);


    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        SystemSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        SystemSocket.onlineCount--;
    }

    private static final Map<String, Session> users = new ConcurrentHashMap();




    @OnOpen
    public void Open(@PathParam("authcode") String authcode, Session session) throws IOException {


         WSUtils.redisUtils.set(authcode,authcode, Constants.TOKEN_EXPIRE);
         users.put(authcode,session);

        addOnlineCount();//在线数加1
        log.info("有一连接打开！当前在线人数为" + getOnlineCount());


    }


    @OnMessage
    public void ReceiveMsg(@PathParam("authcode") String authcode,String msg, Session session)throws Exception{


    }



    @OnClose
    public void disConnect(Session session,@PathParam("authcode") String authcode) {
            subOnlineCount();
            WSUtils.redisUtils.del(authcode);
            users.remove(session);

    }


    @OnError
    public void onError(Session session, Throwable error,@PathParam("authcode") String authcode) {
        subOnlineCount();
        WSUtils.redisUtils.del(authcode);
        users.remove(session);

    }


    /**
     * 群发
     *
     */

    public static void sendMessage(int code,String msg,Object data){

        JSONObject object=JSONObject.fromObject(MessageData.ofSuccess(code,msg,data));

        Set<String> set=users.keySet();
       for(String session:set){

           if(!users.get(session).isOpen()){
               users.remove(users.get(session));
               continue;
           }
           users.get(session).getAsyncRemote().sendText(object.toString());
       }


    }




    // 指定用户
    public static void sendUser(String user, int code,String msg,Object data)  {

        JSONObject object=JSONObject.fromObject(MessageData.ofSuccess(code,msg,data));
        Session set=users.get(user);
        if(!set.isOpen()){
            users.remove(set);
        }
        if(set!=null){

            set.getAsyncRemote().sendText(object.toString());
        }

    }


    public static void openLive(String archor){

        List<OutFansInfo> fans= WSUtils.anchorServices.getFansByAid(archor);
        String name=WSUtils.userServices.getUserByUserName(archor).getNickName();

        for(OutFansInfo info:fans){
            sendUser(info.getUid(),2,"你关注的主播:"+name+"开播了",null);
        }

    }



    public static void sendNotice(String user, SysNotice sysNotice ){

        if(user!=null&&!user.isEmpty()){

            sendUser(user,3,"公告",sysNotice);

        }else{

            sendMessage(3,"公告",sysNotice);
        }

    }


    public static void sendMonitor(String user, boolean start,int speed ){
        int code=0;
        if(start){
            code=1;
        }

        if(user!=null&&!user.isEmpty()){


            sendUser(user,code,"开启",speed);

        }else{

            sendMessage(code,"关闭",speed);
        }

    }


}
