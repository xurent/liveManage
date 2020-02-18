package com.xurent.live.model;

import lombok.Data;

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

    private Integer kind;    //分类

    private String announcement;//公告

    private Date date; //时间

    private Integer state=-1; // -1未来创建 0未开播 1直播中


}
