/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.google.common.collect.Maps;
import com.pos.common.util.validation.FieldChecker;
import com.pos.user.dao.CustomerDao;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    public CustomerDto findById(Long userId, Boolean enable) {
        FieldChecker.checkEmpty(userId, "userId");

        return customerDao.findByUserIdAndEnable(userId, enable);
    }

    @Override
    public Map<Long, CustomerDto> getCustomerDtoMapById(List<Long> userIds) {
        FieldChecker.checkEmpty(userIds, "userIds");
        List<CustomerDto> customerDtos = findInUserIds(userIds, null);
        if (CollectionUtils.isEmpty(customerDtos)) {
            return Maps.newLinkedHashMap();
        }

        Map<Long, CustomerDto> customerDtoMap = Maps.newLinkedHashMap();
        for (CustomerDto customerDto : customerDtos) {
            customerDtoMap.put(customerDto.getId(), customerDto);
        }

        return customerDtoMap;
    }

    @Override
    public List<CustomerDto> findInUserIds(List<Long> userIds, Boolean enable) {
        FieldChecker.checkEmpty(userIds, "userIds");

        return customerDao.findCustomersInUserIds(userIds, enable);
    }
}
