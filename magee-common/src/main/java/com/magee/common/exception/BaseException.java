package com.magee.common.exception;

/**
 * 全局异常
 * @author magee
 * @version 1.0
 */
public class BaseException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    /**
     * 错误提示
     */
    private String message;
    /**
     * 错误明细，内部调试错误
     *
     */
    private String detailMessage;

    public BaseException(){}

    public BaseException(String message){
        this.message = message;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    public String getMessage(){
        return message;
    }

    public BaseException setMessage(String message){
        this.message = message;
        return this;
    }

    public BaseException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }

}
