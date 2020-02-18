package com.xurent.live.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  MessageCode {
    /**
     * 响应状态码
     */
    SUCCESS(0,"success"),
    ERROR(1,"error"),
    ERROR_UN_AUTH(2,"未授权，请重新登录后尝试"),
    ERROR_NUMBER_FORMAT(3,"参数类型不匹配");

    private int code;
    private String desc;

}

