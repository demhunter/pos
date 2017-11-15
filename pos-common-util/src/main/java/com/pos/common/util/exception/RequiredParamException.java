/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.exception;

/**
 * 缺少必须参数的异常.
 *
 * @author wayne
 * @version 1.0, 2016/7/22
 */
public class RequiredParamException extends ValidationException {

    private static final long serialVersionUID = 7174191333877481023L;

    public RequiredParamException() {
        super();
    }

    public RequiredParamException(String message) {
        super(message);
    }

    public RequiredParamException(Throwable cause) {
        super(cause);
    }

    public RequiredParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}