/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service_v.impl;

import com.pos.common.util.validation.FieldChecker;
import com.pos.user.dao.v1_0_0.CustomerDao;
import com.pos.user.dto.v1_0_0.CustomerDto;
import com.pos.user.service_v.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 客户相关Service实现类
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    @Override
    public CustomerDto findById(Long userId, boolean disable) {
        FieldChecker.checkEmpty(userId, "userId");

        return customerDao.findByUserIdAndEnable(userId, disable ? null : true);
    }
}
