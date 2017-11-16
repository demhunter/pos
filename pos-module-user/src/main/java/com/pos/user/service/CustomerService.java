/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.user.dto.customer.CustomerDto;

import java.util.List;
import java.util.Map;

/**
 * 客户相关Service
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
public interface CustomerService {

    /**
     * 根据用户ID查询客户信息.
     *
     * @param userId 用户ID
     * @param enable 账号状态(true：启用，false：禁用，null：不限)
     */
    CustomerDto findById(Long userId, Boolean enable);

    /**
     * 查询客户信息
     *
     * @param userIds
     * @return
     */
    Map<Long, CustomerDto> getCustomerDtoMapById(List<Long> userIds);

    /**
     * 查询指定的一组客户信息.
     *
     * @param userIds 一组用户ID
     * @param enable  true：只返回启用，false：只返回禁用，null：不限
     */
    List<CustomerDto> findInUserIds(List<Long> userIds, Boolean enable);
}
