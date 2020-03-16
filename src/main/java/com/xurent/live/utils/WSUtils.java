package com.xurent.live.utils;

import com.xurent.live.service.AnchorService;
import com.xurent.live.service.LiveRoomService;
import com.xurent.live.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WSUtils {


    @Autowired
    private  RedisUtil redisUtil;

    @Autowired
    private LiveRoomService roomService;

    @Autowired
    private AnchorService anchorService;

    @Autowired
    private UserService userService;

    public static RedisUtil redisUtils;

    public static  LiveRoomService roomServices;

    public static  AnchorService anchorServices;

    public static  UserService userServices;


    @PostConstruct
    public void init() {

        redisUtils=this.redisUtil;
        roomServices=this.roomService;
        anchorServices=this.anchorService;
        userServices=this.userService;
    }

}
