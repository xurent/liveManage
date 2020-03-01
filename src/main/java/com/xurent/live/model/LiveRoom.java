package com.xurent.live.model;

import com.xurent.live.model.out.UserInfo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "tb_room")
@Data
public class LiveRoom implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String username;  //主播ID

    private String title;    //标题

    private String roomImg;   //房间封面

    private String kind;    //分类

    private String announcement;//公告
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date; //时间

    private Integer state=0; //  0未开播 1直播中

    private Integer online=0; //在线人数


}
