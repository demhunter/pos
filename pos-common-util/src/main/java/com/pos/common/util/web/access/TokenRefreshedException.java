/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.access;

/**
 * 令牌被刷新的异常.
 *
 * @author wayne
 * @version 1.0, 2017/2/23
 */
public class TokenRefreshedException extends Exception {

    private static final long serialVersionUID = -1665017481301671636L;

    public TokenRefreshedException() {
        super();
    }

    public TokenRefreshedException(String message) {
        super(message);
    }

    public TokenRefreshedException(Throwable cause) {
        super(cause);
    }

    public TokenRefreshedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenRefreshedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}