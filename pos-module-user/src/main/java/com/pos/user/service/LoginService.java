/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.user.dto.login.LoginInfoDto;
import com.pos.user.dto.UserDto;

/**
 * 用户登录服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public interface LoginService {

    /**
     * 用户登录验证.
     *
     * @param loginInfo 登录信息
     * @return data根据登录类型手动转型为具体类型
     */
    ApiResult<? extends UserDto> login(LoginInfoDto loginInfo);

}
