/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.exception;

/**
 * 验证失败的异常，一般用于字段校验.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 9092054852956591561L;

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}