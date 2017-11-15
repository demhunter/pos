/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.exception;

import com.pos.common.util.mvc.support.ApiError;

import java.io.Serializable;

/**
 * 用户在其它设备登录（顶号）的ApiError
 *
 * @author wangbing
 * @version 1.0, 2017/7/18
 */
public class UserTokenLoggedOtherError extends ApiError implements Serializable {

    private static final long serialVersionUID = 5484705303073981262L;

    public UserTokenLoggedOtherError() {
        super(UserErrorCode.USER_TOKEN_LOGGED_OTHER, UserErrorCode.USER_TOKEN_LOGGED_OTHER.getMessage());
    }
}
