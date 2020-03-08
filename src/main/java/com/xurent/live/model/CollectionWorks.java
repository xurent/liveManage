package com.xurent.live.model;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "tb_record_works")
public class CollectionWorks implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String uid;//用户ID

    private Integer workId;//作品ID


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
