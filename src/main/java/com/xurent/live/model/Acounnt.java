package com.xurent.live.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tb_acount")
public class Acounnt {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String uid;

    private long acount;


}
