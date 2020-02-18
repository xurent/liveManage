package com.xurent.live.advice;

import com.xurent.live.exception.GlobalException;
import com.xurent.live.exception.UnAuthException;
import com.xurent.live.model.message.MessageCode;
import com.xurent.live.model.message.MessageData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = UnAuthException.class)
    public MessageData handleUnAuthException(UnAuthException ex){
        MessageData messageData = new MessageData();
        messageData.setCode(MessageCode.ERROR_UN_AUTH.getCode());
        messageData.setMsg(MessageCode.ERROR_UN_AUTH.getDesc());
        messageData.setData(ex.getMessage());
        return messageData;
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public MessageData handleNumberFormatException(NumberFormatException ex){
        MessageData messageData = new MessageData();
        messageData.setCode(MessageCode.ERROR_NUMBER_FORMAT.getCode());
        messageData.setMsg(MessageCode.ERROR_NUMBER_FORMAT.getDesc());
        messageData.setData(ex.getMessage());
        return messageData;
    }

    @ExceptionHandler(value = {GlobalException.class,Exception.class})
    public MessageData handleGlobalException(Exception ex){
        MessageData messageData = new MessageData();
        messageData.setCode(MessageCode.ERROR.getCode());
        messageData.setMsg(MessageCode.ERROR.getDesc());
        messageData.setData(ex.getMessage());
        return messageData;
    }
}
