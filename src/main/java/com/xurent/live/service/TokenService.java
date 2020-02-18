package com.xurent.live.service;

import com.xurent.live.model.User;
import com.xurent.live.model.out.UserInfo;

public interface TokenService {


    public UserInfo getUserInfo();

    public Integer Uid();

    public String getToken();

}
