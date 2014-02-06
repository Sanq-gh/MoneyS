package com.sanq.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 22.11.13
 * Time: 11:09
 */
public class DBException extends Exception{
    public DBException(String detailMessage) {
        super(detailMessage);
    }

    public DBException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DBException(Throwable throwable) {
        super(throwable);
    }

}
