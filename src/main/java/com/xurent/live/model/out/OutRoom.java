package com.xurent.live.model.out;

import com.xurent.live.model.LiveRoom;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class OutRoom  {

    private Integer id;

    private String username;  //主播ID

    private String title;    //标题

    private String roomImg;   //房间封面

    private String kind;    //分类

    private String announcement;//公告
    private Date date; //时间

    private Integer state=0; //  0未开播 1直播中

    private Integer online=0; //在线人数
    private String nickname;

    private String anchorImg;



}
