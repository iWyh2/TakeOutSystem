package com.wyh.TakeOut.common;

//自定义业务异常
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
