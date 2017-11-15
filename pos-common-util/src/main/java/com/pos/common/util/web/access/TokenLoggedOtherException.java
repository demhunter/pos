/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.access;

/**
 * 账号在另一设备登录异常（顶号处理）.
 *
 * @author wangbing
 * @version 1.0, 2017/7/18
 */
public class TokenLoggedOtherException extends Exception {

    private static final long serialVersionUID = 6567610167402989471L;

    public TokenLoggedOtherException() {
        super();
    }

    public TokenLoggedOtherException(String message) {
        super(message);
    }

    public TokenLoggedOtherException(Throwable cause) {
        super(cause);
    }

    public TokenLoggedOtherException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenLoggedOtherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
