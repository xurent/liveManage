package com.xurent.live.controller;


import com.xurent.live.common.Constants;
import com.xurent.live.model.User;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.model.out.UserInfo;
import com.xurent.live.service.TokenService;
import com.xurent.live.service.UserService;
import com.xurent.live.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Object updateUserInfo(UserInfo userInfo){

        if(userInfo!=null&&userInfo.getUsername()!=null){

            User u= (User) redisUtil.get(iToken.getToken());
            u.setNickName(userInfo.getNickName());
            u.setIntroduction(u.getIntroduction());
            u.setHeadImg(u.getHeadImg());
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



}
