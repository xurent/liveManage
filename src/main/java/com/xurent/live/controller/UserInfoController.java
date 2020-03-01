package com.xurent.live.controller;


import com.xurent.live.common.Constants;
import com.xurent.live.model.User;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.model.out.UserInfo;
import com.xurent.live.service.TokenService;
import com.xurent.live.service.UserService;
import com.xurent.live.utils.CookieUtils;
import com.xurent.live.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/liveDance")
@Controller
public class UserInfoController {


    @Autowired
    private UserService userService;

    @Autowired
    private TokenService iToken;


    @Autowired
    private RedisUtil redisUtil;


    @ResponseBody
    @PostMapping("/updateUserInfo")
    public Object updateUserInfo(UserInfo userInfo, HttpServletRequest request, HttpServletResponse response){

        System.out.println(userInfo.toString());

        String update_token=CookieUtils.getCookie(request.getCookies(),"update");
        String userID= (String) redisUtil.get(update_token);
        if(userID!=null){
            User u=userService.getUserByUserName(userID);
            u.setNickName(userInfo.getNickName());
            u.setIntroduction(userInfo.getIntroduction());
            u.setHeadImg(userInfo.getHeadImg());
            userService.updateUserInfoByUsername(u);
            CookieUtils.delCookie(request,response);
            return MessageData.ofSuccess("编辑个人资料成功");
        }

        else if(userInfo!=null&&userInfo.getNickName()!=null){

            User u= (User) redisUtil.get(iToken.getToken());
            u.setNickName(userInfo.getNickName());
            u.setIntroduction(userInfo.getIntroduction());
            u.setHeadImg(userInfo.getHeadImg());
            userService.updateUserInfoByUsername(u);
            redisUtil.set(iToken.getToken(),u, Constants.TOKEN_EXPIRE);
            return MessageData.ofSuccess("编辑个人资料成功");
        }



        return MessageData.ofError("修改失败");
    }


    @ResponseBody
    @PostMapping("/updatePwd")
    public Object updatePwd(@RequestParam("password")String pwd){

        User u= (User) redisUtil.get(iToken.getToken());
        u.setPassword(pwd);
        userService.updateUserInfoByUsername(u);
        return MessageData.ofError("修改成功");
    }

    @ResponseBody
    @GetMapping("/getUserInfo")
    public Object getUserInfo(@RequestParam("userId")String uid){

       User u= userService.getUserByUserName(uid);
       UserInfo info =new UserInfo();
       if(u!=null){
           info.setHeadImg(u.getHeadImg());
           info.setIntroduction(u.getIntroduction());
           info.setNickName(u.getNickName());
           info.setUsername(u.getUserName());
       }else{
           return  MessageData.ofError("用户不存在");
       }

        return  MessageData.ofSuccess(u);
    }

    @ResponseBody
    @GetMapping("/getMyInfo")
    public Object getMyInfo(){

        UserInfo info=iToken.getUserInfo();

        return  MessageData.ofSuccess(info);
    }


}
