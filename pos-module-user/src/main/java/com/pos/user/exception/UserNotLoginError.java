/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.exception;

import com.pos.common.util.mvc.support.ApiError;

import java.io.Serializable;

/**
 * 用户未登录的ApiError.
 *
 * @author wayne
 * @version 1.0, 2016/6/29
 */
public final class UserNotLoginError extends ApiError implements Serializable {

    private static final long serialVersionUID = 6220374455666420003L;

    public UserNotLoginError() {
        super(UserErrorCode.USER_NOT_LOGIN, UserErrorCode.USER_NOT_LOGIN.getMessage());
    }

}