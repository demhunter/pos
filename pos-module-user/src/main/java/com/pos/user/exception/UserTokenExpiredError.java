/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.exception;

import com.pos.common.util.mvc.support.ApiError;

import java.io.Serializable;

/**
 * 用户令牌过期的ApiError.
 *
 * @author wayne
 * @version 1.0, 2017/2/14
 */
public class UserTokenExpiredError extends ApiError implements Serializable {

    private static final long serialVersionUID = 8669283813332316655L;

    public UserTokenExpiredError() {
        super(UserErrorCode.USER_TOKEN_EXPIRED, UserErrorCode.USER_TOKEN_EXPIRED.getMessage());
    }

}