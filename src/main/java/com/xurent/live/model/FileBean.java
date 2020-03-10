package com.xurent.live.model;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="tb_file")
public class FileBean implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String userId; //用户ID
    private String nickName;//昵称
    private String fileName;
    private String thumbleImg;//预览图
    private String  url;
    private Integer type; //  0图片  1 视频 2VR视频
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer likeNumber=0;

}
