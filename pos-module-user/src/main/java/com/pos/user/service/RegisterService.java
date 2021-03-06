/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.user.constant.CustomerType;
import com.pos.user.dto.LoginInfoDto;
import com.pos.user.dto.UserRegConfirmDto;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.manager.ManagerDto;

/**
 * 用户注册服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/6/7
 */
public interface RegisterService {

    /**
     * 添加一个C端用户账号.
     *
     * @param registerInfoDto 新增用户信息内容
     * @param setLoginInfo    是否记录登录信息
     * @return 新增结果
     */
    ApiResult<CustomerDto> addCustomer(LoginInfoDto registerInfoDto, boolean setLoginInfo, CustomerType customerType);

    /**
     * 添加一个平台管理员.
     *
     * @param managerDto   管理员信息
     * @param createUserId 创建者ID
     * @return
     */
    ApiResult addManager(ManagerDto managerDto, Long createUserId);

}
