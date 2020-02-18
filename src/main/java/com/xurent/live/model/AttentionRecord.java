package com.xurent.live.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;



@Data
@Entity
@Table(name = "tb_attention")
public class AttentionRecord implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String userId;  //用户Id

    private String roomId;  //房间ID(即主播ID)

    private Integer type=0; //0未关注 1关注

    private long acoount=0; //金克拉


}
