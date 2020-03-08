package com.xurent.live.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tb_music")
public class Music {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    private String url;
    private String imageUrl;

}
