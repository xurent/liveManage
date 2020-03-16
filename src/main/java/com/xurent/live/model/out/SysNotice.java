package com.xurent.live.model.out;


import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class SysNotice {

    private Integer type=0;
    private String msg;
    private String data;

}
