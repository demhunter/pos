/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.exception;

/**
 * 程序初始化失败的异常.
 *
 * @author wayne
 * @version 1.0, 2016/6/16
 */
public class InitializationException extends RuntimeException {

    private static final long serialVersionUID = -6796137957881202942L;

    public InitializationException() {
        super();
    }

    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(Throwable cause) {
        super(cause);
    }

    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}