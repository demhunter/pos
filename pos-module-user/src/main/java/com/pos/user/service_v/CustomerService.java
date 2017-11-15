/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service_v;

import com.pos.user.dto.v1_0_0.CustomerDto;

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
     * @param userId  用户ID
     * @param disable 是否返回被禁用的账号(true：需要返回被禁用的账号，false：不限)
     */
    CustomerDto findById(Long userId, boolean disable);
}
