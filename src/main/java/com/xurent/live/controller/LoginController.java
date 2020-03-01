package com.xurent.live.controller;


import com.xurent.live.common.Constants;
import com.xurent.live.model.User;
import com.xurent.live.model.message.MessageData;
import com.xurent.live.model.out.UserInfo;
import com.xurent.live.service.TokenService;
import com.xurent.live.service.UserService;
import com.xurent.live.utils.CookieUtils;
import com.xurent.live.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@RequestMapping("/liveDance")
@RestController
@Api(tags = "登录授权接口")
public class LoginController {


    @Autowired
    private UserService userService;


    private  final RedisUtil redisUtil;

    @Autowired
    private TokenService iToken;

    @Autowired
    public LoginController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @ApiOperation(value = "请求登录",notes = "手机即账号")
    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestParam("username") String phone, @RequestParam("password") String pwd , HttpServletResponse response, HttpServletRequest request){

        System.out.println(phone+","+pwd);
        boolean correct=userService.checkPassword(phone,pwd);

        if(correct){
            User u=userService.getByPhone(phone);
            String token=UUID.randomUUID().toString();
            CookieUtils.setCookie(response,"token",token, Constants.TOKEN_EXPIRE);
            redisUtil.set(token,u,Constants.TOKEN_EXPIRE);
            return MessageData.ofSuccess("登录成功!",token);
        }

        return  MessageData.ofError("密码错误!");
    }


    @ApiOperation(value = "请求注册",notes = "注册账号接口")
    @ResponseBody
    @PostMapping("/register")
    public Object register(@RequestParam("password")String pwd,@RequestParam("username")String phone,HttpServletResponse response){

        if(phone.isEmpty()||pwd.isEmpty()){
            return  MessageData.ofError("手机号和密码不能为null");
        }

        if(userService.getByPhone(phone)!=null){
            return  MessageData.ofError("请勿重复注册");
        }

        User user=new User();
        user.setPassword(pwd);
        user.setPhone(phone);
        int number=(int)(Math.random()*10000000+1000000);
        while (userService.getUserByUserName(String.valueOf(number))!=null){
            number=(int)(Math.random()*10000000+1000000);;
        }
        user.setUserName(String.valueOf(number));
        userService.Add(user);
        String token=UUID.randomUUID().toString();
        CookieUtils.setCookie(response,"update",token, Constants.TOKEN_EXPIRE);
        redisUtil.set(token,user.getUserName(),Constants.TOKEN_EXPIRE);

        return  MessageData.ofSuccess("注册成功!",number);
    }


    @ResponseBody
    @ApiOperation(value = "登出",notes = "使得Token失效，本系统不再保存用户状态")
    @GetMapping("/logout")
    public Object logout(HttpServletRequest request,HttpServletResponse response){

        String token = request.getHeader(Constants.TOKEN_STRING);
        System.out.println(token);
        if(token==null){
            token = CookieUtils.getCookie(request.getCookies(),Constants.TOKEN_STRING);
        }

        if(token==null){return MessageData.ofError("登出失败!");}
        User u= (User) redisUtil.get(token);
        if(u!=null){

            CookieUtils.delCookie(request,response);
            redisUtil.remove(token);
           return MessageData.ofSuccess("登出成功!");
        }


        return MessageData.ofError("登出失败!");
    }

    @ApiOperation(value = "登录状态",notes = "判断登录是否有效，用于自动登录")
    @ResponseBody
    @GetMapping("/expire")
    public Object TokenExpire(/*@RequestParam("username")String username*/){
        String itoken=iToken.getToken();
      /*  if(!iToken.getUserInfo().getUsername().equals(username)){

            redisUtil.remove(itoken);
            return  MessageData.ofError();
        }*/
        if(redisUtil.hasKey(itoken)){
            if(redisUtil.getExpire(itoken)>1){
                redisUtil.expire(itoken,Constants.TOKEN_EXPIRE);//重新更新有效时间
                return MessageData.ofSuccess("token有效",iToken.getUserInfo());
            }
        }
        return  MessageData.ofError();
    }


}
