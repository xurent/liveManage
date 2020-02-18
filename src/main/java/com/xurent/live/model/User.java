package com.xurent.live.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "tb_user")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String userName;


    private  String password;

    private  String nickName;

    private  String phone;


    private  String headImg;

    private  String Introduction;


}
