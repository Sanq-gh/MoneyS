package com.sanq.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 22.11.13
 * Time: 11:11
 */
public class TestException extends RuntimeException {
    public TestException(String detailMessage) {
        super(detailMessage);
    }

    public TestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TestException(Throwable throwable) {
        super(throwable);
    }
}
