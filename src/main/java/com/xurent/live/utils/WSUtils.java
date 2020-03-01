package com.xurent.live.utils;

import com.xurent.live.service.LiveRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WSUtils {


    @Autowired
    private  RedisUtil redisUtil;

    @Autowired
    private LiveRoomService roomService;

    public static RedisUtil redisUtils;

    public static  LiveRoomService roomServices;


    @PostConstruct
    public void init() {

        redisUtils=this.redisUtil;
        roomServices=this.roomService;

    }

}
