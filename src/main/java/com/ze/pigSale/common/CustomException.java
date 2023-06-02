package com.ze.pigSale.common;

/**
 * 自定义业务异常类
 * @author zeb
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
