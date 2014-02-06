package com.sanq.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 05.06.13
 * Time: 15:07
 *
 * 1. db
 * 2.save  checked
 * 3.time and date  checked
 *
 *
 */
public class SavingException extends Exception {
    public SavingException(String detailMessage) {
        super(detailMessage);
    }

    public SavingException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SavingException(Throwable throwable) {
        super(throwable);
    }
}
