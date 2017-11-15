/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.login.RegisterInfoDto;

/**
 * 用户注册服务接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public interface RegisterService {

    /**
     * 添加一个C端用户账号.
     *
     * @param registerInfo 新增用户信息内容
     * @param setLoginInfo 是否记录登录信息
     * @return 新增结果
     */
    ApiResult<CustomerDto> addCustomer(RegisterInfoDto registerInfo, boolean setLoginInfo);
}
