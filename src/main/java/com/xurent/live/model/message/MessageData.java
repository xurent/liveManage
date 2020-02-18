package com.xurent.live.model.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {

    /**
     * 状态码 消息 数据
     */
    @ApiModelProperty(value = "响应状态码",example = "0")
    private  int code;
    @ApiModelProperty(value = "响应描述",example = "success")
    private String msg;
    @ApiModelProperty(value = "响应数据",example = "<T>")
    private  Object data;

    private MessageData(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.code == MessageCode.SUCCESS.getCode();
    }

    public static MessageData ofSuccess(){
        return new MessageData(MessageCode.SUCCESS.getCode(),MessageCode.SUCCESS.getDesc());
    }

    public static MessageData ofSuccess(Object o){
        return new MessageData(MessageCode.SUCCESS.getCode(),MessageCode.SUCCESS.getDesc(),o);
    }

    public static MessageData ofSuccess(int code,String msg,Object o){
        return new MessageData(code,msg,o);
    }

    public static MessageData ofError(int code,String msg,Object o){
        return new MessageData(code,msg,o);
    }

    public static MessageData ofSuccess(String msg){
        return new MessageData(MessageCode.SUCCESS.getCode(),msg);
    }

    public static MessageData ofSuccess(String msg,Object o){
        return new MessageData(MessageCode.SUCCESS.getCode(),msg,o);
    }

    public static MessageData ofError(){
        return new MessageData(MessageCode.ERROR.getCode(),MessageCode.ERROR.getDesc());
    }

    public static MessageData ofError(String msg){
        return new MessageData(MessageCode.ERROR.getCode(),msg);
    }

    public static MessageData ofError(Object o){
        return new MessageData(MessageCode.ERROR.getCode(),MessageCode.ERROR.getDesc(),o);
    }

    public static MessageData ofError(String msg,Object o){
        return new MessageData(MessageCode.ERROR.getCode(),msg,o);
    }

}