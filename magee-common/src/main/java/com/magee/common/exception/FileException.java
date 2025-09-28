package com.magee.common.exception;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class FileException extends RuntimeException{
    public FileException(String message ){
        super(message);
    }
    public FileException(Throwable cause){
        super(cause);
    }
    public FileException(String message, Throwable cause){
        super(message,cause);
    }
}
