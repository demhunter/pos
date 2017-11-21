/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.user.dto.LoginInfoDto;
import com.pos.user.dto.UserDto;
import com.pos.user.dto.UserLoginDto;

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
     * @param userLoginDto 用户登录信息
     * @param loginInfoDto 用户登录拓展信息，v1.3.1 主要为IP和设备相关信息
     * @return data根据登录类型手动转型为具体类型
     */
    ApiResult<? extends UserDto> login(UserLoginDto userLoginDto, LoginInfoDto loginInfoDto);

}
