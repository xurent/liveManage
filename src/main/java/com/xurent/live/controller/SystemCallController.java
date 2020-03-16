package com.xurent.live.controller;


import com.xurent.live.model.message.MessageData;
import com.xurent.live.model.out.SysNotice;
import com.xurent.live.sever.SystemSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("System")
public class SystemCallController {



    @Autowired
    private SysNotice sysNotice;


    /**
     * 发公告
     * @param type
     * @param msg
     * @param data
     * @param user
     * @return
     */
    @PostMapping("/sendNotice")
    public Object sendNotice(@RequestParam("type") Integer type,@RequestParam("msg") String msg,@RequestParam("data") String data
    ,@RequestParam(value = "uid",required = false)String user){

        sysNotice.setData(data);
        sysNotice.setType(type);
        sysNotice.setMsg(msg);
        SystemSocket.sendNotice(user,sysNotice);

        return MessageData.ofSuccess("ok");
    }



    @PostMapping("/Monitor")
    public Object Monitor(@RequestParam("start") boolean type,@RequestParam("speed") Integer speed
            ,@RequestParam(value = "uid",required = false)String user){

       SystemSocket.sendMonitor(user,type,speed);

        return MessageData.ofSuccess("ok");
    }



}
