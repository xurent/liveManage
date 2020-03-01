package com.xurent.live.model;


import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class LiveUrl {


    private String pushUrl;
    private String playUrlRtmp;
    private String playUrlFlv;
    private String playUrlHls;




}
