/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.exception;

/**
 * 不合法的参数异常, 如类型错误, 格式错误, 取值范围错误等等.
 *
 * @author wayne
 * @version 1.0, 2016/7/22
 */
public class IllegalParamException extends ValidationException {

    private static final long serialVersionUID = 2948913998295227214L;

    public IllegalParamException() {
        super();
    }

    public IllegalParamException(String message) {
        super(message);
    }

    public IllegalParamException(Throwable cause) {
        super(cause);
    }

    public IllegalParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}