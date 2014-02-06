package com.sanq.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 22.11.13
 * Time: 11:08
 */
public class DateCheckedException extends Exception{
    public DateCheckedException(String detailMessage) {
        super(detailMessage);
    }

    public DateCheckedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DateCheckedException(Throwable throwable) {
        super(throwable);
    }

}
