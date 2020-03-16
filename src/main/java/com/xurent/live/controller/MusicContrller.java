package com.xurent.live.controller;


import com.xurent.live.dao.MusicDao;
import com.xurent.live.model.Music;
import com.xurent.live.model.message.MessageData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/music")
@Api(tags = "音乐资源接口")
public class MusicContrller {


    @Autowired
    private MusicDao musicDao;


    @ResponseBody
    @GetMapping("/getMusic")
    public Object getMusci(){

        List<Music> musics=musicDao.findAll();
        return MessageData.ofSuccess("成功",musics);
    }

}
