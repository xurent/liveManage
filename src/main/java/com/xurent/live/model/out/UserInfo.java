package com.xurent.live.model.out;


import lombok.Data;

@Data
public class UserInfo {


    private  String username;

    private  String nickName;

    private  String phone;


    private  String headImg;

    private  String Introduction;


    public UserInfo() {
    }

    public UserInfo(String username, String nickName, String phone,  String headImg, String introduction) {
        this.username = username;
        this.nickName = nickName;
        this.phone = phone;
        this.headImg = headImg;
        Introduction = introduction;
    }
}
