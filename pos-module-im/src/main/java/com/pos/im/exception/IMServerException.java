/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.exception;

/**
 * 向IM Server发起请求失败的异常类.
 *
 * @author wayne
 * @version 1.0, 2016/7/14
 */
public class IMServerException extends Exception {

    private static final long serialVersionUID = 6167533854529502745L;

    public IMServerException() {
        super();
    }

    public IMServerException(String message) {
        super(message);
    }

    public IMServerException(Throwable cause) {
        super(cause);
    }

    public IMServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public IMServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}