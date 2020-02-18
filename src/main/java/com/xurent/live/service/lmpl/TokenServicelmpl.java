package com.xurent.live.service.lmpl;

import com.xurent.live.common.Constants;
import com.xurent.live.exception.UnAuthException;
import com.xurent.live.model.User;
import com.xurent.live.model.out.UserInfo;
import com.xurent.live.service.TokenService;
import com.xurent.live.utils.CookieUtils;
import com.xurent.live.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service("tokenService")
public class TokenServicelmpl implements TokenService {

    private final RedisUtil redisUtil;


    @SuppressWarnings("all")
    private HttpServletRequest getRequest(){
        return  ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

    }

    @Autowired
    public TokenServicelmpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }


    @Override
    public UserInfo getUserInfo() {
        String token=getToken();
        if(token!=null){
            User user= (User) redisUtil.get(token);
            if(user!=null){
                UserInfo info=new UserInfo(user.getUserName(), user.getUserName(),user.getPhone(), user.getHeadImg(), user.getIntroduction());
                return info;
            }

        }
        throw new UnAuthException("token无效，请登录后重新尝试");
    }

    @Override
    public Integer Uid() {
        String token=getToken();
        if(token!=null){
            User user= (User) redisUtil.get(token);
            if(user!=null&&user.getId()!=null){
                return user.getId();
            }
        }
        throw new UnAuthException("token无效，请登录后重新尝试");
    }


    @Override
    public String getToken() {
        HttpServletRequest request = this.getRequest();
        String Token=CookieUtils.getCookie(request.getCookies(), Constants.TOKEN_STRING);
        if(Token!=null){
            return Token;
        }

        throw new UnAuthException("无法获取到token，请重新登录后尝试");
    }



}
