/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.access;

/**
 * 会话过期的异常.
 *
 * @author wayne
 * @version 1.0, 2017/2/15
 */
public class TokenExpiredException extends Exception {

    private static final long serialVersionUID = 8779994098486573588L;

    public TokenExpiredException() {
        super();
    }

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(Throwable cause) {
        super(cause);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}