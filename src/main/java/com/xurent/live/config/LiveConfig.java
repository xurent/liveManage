package com.xurent.live.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class LiveConfig {



    @Value("${liveDance.bizId}")
    private String bizId;

    @Value("${liveDance.key}")
    private String key;

    @Value("${liveDance.playUrl}")
    private String playUrl;

}
