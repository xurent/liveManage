package com.xurent.live.utils;

import com.xurent.live.common.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: HXR
 * @Date: 2019/8/1 11:28
 * @Info:
 */
public class CookieUtils {

    public  static String getCookie(Cookie[] cookie, String key){
        if(key==null||cookie==null) return null;
        for (Cookie c :cookie){
            if(c.getName().equals(key)){
                return c.getValue();
            }
        }
        return null;
    }

    public static  void setCookie(HttpServletResponse response, String key, Object value){
        Cookie cookie=new Cookie(key,value.toString());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static  void setCookie(HttpServletResponse response, String key, Object value, int time){
        Cookie cookie=new Cookie(key,value.toString());
        cookie.setPath("/");
        cookie.setMaxAge(time);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static  void setCookie(HttpServletResponse response, String key, Object value, String path, int time){
        Cookie cookie=new Cookie(key,value.toString());
        cookie.setPath(path);
        cookie.setMaxAge(time);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static boolean delCookie(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {

                if (Constants.TOKEN_STRING.equals(cookie.getName())) {
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                    return true;
                }
            }
        }
        return false;
    }


}
