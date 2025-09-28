package com.magee.common.exception;

/**
 * 业务异常
 * @author magee
 * @version 1.0
 */
public class ServiceException extends BaseException{

    public ServiceException(){}

    public ServiceException(String message){
        super(message);
    }
}
