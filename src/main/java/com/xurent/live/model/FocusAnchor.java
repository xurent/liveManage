package com.xurent.live.model;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_anchor")
@Data
public class FocusAnchor implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private  String uid;//用户ID
    private  String aid;//主播id

    private Integer type=0; //0未关注 1关注

    private long acoount=0; //金克拉

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

}
