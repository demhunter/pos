/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.user.dto.LoginInfoDto;
import com.pos.user.dto.employee.EmployeeDto;
import com.pos.user.constant.CustomerType;
import com.pos.user.domain.User;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.manager.ManagerDto;
import com.pos.user.dto.UserRegConfirmDto;

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
    ApiResult<UserRegConfirmDto> addCustomer(LoginInfoDto registerInfoDto, boolean setLoginInfo, CustomerType customerType);

    /**
     * 已有E端或B端账号时，用户确认C端注册
     *
     * @param confirmInfoDto 用户确认相关的用户拓展信息
     * @param token          token
     * @param setLoginInfo    是否记录登录信息
     * @return 确认结果
     */
    ApiResult<CustomerDto> confirmCustomerRegister(LoginInfoDto confirmInfoDto, String token, boolean setLoginInfo, CustomerType customerType);

    /**
     * 已有E端或B端账号时，用户确认C端注册
     *
     * @param confirmInfoDto 用户确认相关的用户拓展信息
     * @param setLoginInfo    是否记录登录信息
     * @return 确认结果
     */
     ApiResult<CustomerDto> confirmCustomerRegister(LoginInfoDto confirmInfoDto,boolean setLoginInfo, CustomerType customerType);

    /**
     * 添加一个业者账号
     *
     * @param employeeDto  业者信息
     * @param createUserId 创建者id
     * @return 新增结果
     */
    ApiResult addEmployee(EmployeeDto employeeDto, Long createUserId);

    /**
     * 给B端管理员开通业者角色
     *
     * @param employeeDto  业者信息
     * @param createUserId 创建者id
     * @return 开通结果
     */
    ApiResult openManagerEmployeePart(EmployeeDto employeeDto, Long createUserId);

    /**
     * 添加一个B端管理员账号
     *
     * @param user         用户信息
     * @param companyId    公司id
     * @param createUserId 创建者id
     * @param isAvailable  是否开通公司
     * @return 新增结果
     */
    ApiResult addBusiness(User user, Long companyId, boolean isAvailable, Long createUserId);

    /**
     * 添加一个平台管理员.
     *
     * @param managerDto   管理员信息
     * @param createUserId 创建者ID
     * @return
     */
    ApiResult addManager(ManagerDto managerDto, Long createUserId);

}
