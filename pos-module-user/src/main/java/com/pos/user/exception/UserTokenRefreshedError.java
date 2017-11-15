/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.exception;

import com.pos.common.util.mvc.support.ApiError;

import java.io.Serializable;

/**
 * 用户令牌被刷新的ApiError.
 *
 * @author wayne
 * @version 1.0, 2017/2/23
 */
public class UserTokenRefreshedError extends ApiError implements Serializable {

    private static final long serialVersionUID = 6629422591346229094L;

    public UserTokenRefreshedError() {
        super(UserErrorCode.USER_TOKEN_REFRESHED, UserErrorCode.USER_TOKEN_REFRESHED.getMessage());
    }

}