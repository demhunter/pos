/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.google.common.collect.Maps;
import com.pos.user.condition.query.CustomerConsoleListCondition;
import com.pos.user.domain.Customer;
import com.pos.user.dto.customer.CustomerRemarksDto;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Preconditions;
import com.pos.common.util.validation.Validator;
import com.pos.user.condition.query.CustomerListCondition;
import com.pos.user.condition.query.UserListCondition;
import com.pos.user.constant.CustomerType;
import com.pos.user.constant.UserType;
import com.pos.user.dao.CustomerDao;
import com.pos.user.dto.converter.UserDtoConverter;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.service.CustomerService;
import com.pos.user.service.support.UserServiceSupport;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 客户相关服务接口的实现类.
 *
 * @author wayne
 * @version 1.0, 2016/8/4
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    @Resource
    private UserServiceSupport userServiceSupport;

    @Override
    public int getCustomerCount(Boolean available, CustomerConsoleListCondition condition) {
        return customerDao.getCustomerCount(available, condition);
    }

    @Override
    public CustomerDto findById(Long userId, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(userId, "userId");
        return (CustomerDto) userServiceSupport.findById(userId, UserType.CUSTOMER, disable, deleted);
    }

    @Override
    public CustomerDto findByUserName(String userName, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(userName, "userName");
        return (CustomerDto) userServiceSupport.findByUserName(userName, UserType.CUSTOMER, disable, deleted);
    }

    @Override
    public CustomerDto findByUserPhone(String userPhone, boolean disable, boolean deleted) {
        Validator.checkMobileNumber(userPhone);
        return (CustomerDto) userServiceSupport.findByUserPhone(userPhone, UserType.CUSTOMER, disable, deleted);
    }

    @Override
    public List<CustomerDto> findInUserIds(List<Long> userIds, boolean disable, boolean deleted) {
        return customerDao.findCustomersInUserIds(userIds, deleted ? null : false, disable ? null : true);
    }

    @Override
    public boolean update(CustomerDto beforeDto, CustomerDto afterDto) {
        userServiceSupport.checkUpdate(beforeDto, afterDto);
        boolean isUpdated = userServiceSupport.updateUser(beforeDto, afterDto);
        Customer before = UserDtoConverter.convert2Customer(beforeDto);
        Customer after = UserDtoConverter.convert2Customer(afterDto);
        if (!JsonUtils.objectToJson(before).equals(JsonUtils.objectToJson(after))) {
            customerDao.update(after);
            isUpdated = true;
        }

        // 更新用户类型信息, 如果User或Customer被更新, 则强制更新UserClass
        isUpdated = userServiceSupport.updateUserClass(beforeDto, afterDto, isUpdated);
        // 更新后将用户信息刷新到IMServer
        if (isUpdated) {
            // userServiceSupport.refresh2IMServer(beforeDto, afterDto);
        }

        return isUpdated;
    }

    public List<CustomerDto> getCustomers(CustomerConsoleListCondition condition, LimitHelper limitHelper, OrderHelper orderHelper) {
        Preconditions.checkArgument(limitHelper != null, "参数不能为空！");

        return customerDao.getCustomers(condition, limitHelper, orderHelper);
    }

    public List<CustomerDto> getCustomersByCompany(CustomerListCondition condition, LimitHelper limitHelper) {
        Preconditions.checkArgument(condition != null && limitHelper != null, "查询或排序参数不能为空！");

        List<CustomerDto> customerDtos = customerDao.getCustomersByCompany(condition, limitHelper);
        if (CollectionUtils.isEmpty(customerDtos)) {
            return customerDtos;
        }

        // 屏蔽手机号中间四位
        for (CustomerDto customerDto : customerDtos) {
            String originalPhone = customerDto.getUserPhone();
            String newPhone = originalPhone.substring(0, 3) + "****" + originalPhone.substring(7);
            customerDto.setUserPhone(newPhone);
        }

        return customerDtos;
    }

    public int getCustomerCountByCompany(CustomerListCondition condition) {
        Preconditions.checkNotNull(condition, "查询条件不能为空！");

        return customerDao.getCustomerCountByCompany(condition);
    }

    @Override
    public List<Long> findUserIdInfo(UserListCondition userListCondition) {
        FieldChecker.checkEmpty(userListCondition, "userListCondition");
        if (StringUtils.isEmpty(userListCondition.getSearchKey()) && StringUtils.isEmpty(userListCondition.getUserPhone())) {
            return null;
        }
        return customerDao.findUserIdInfo(userListCondition);
    }

    @Override
    public Map<Long, CustomerDto> getCustomerDtoMapById(List<Long> userIds) {
        FieldChecker.checkEmpty(userIds, "userIds");
        List<CustomerDto> customerDtos = findInUserIds(userIds, true, false);
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
    public void updateCustomerInfo(Long userId, CustomerType customerType) {
        FieldChecker.checkEmpty(userId, "客户Id");

        Customer customer = findCustomerById(userId);
        if (customer == null) {
            throw new IllegalStateException("未查到对应的用户信息");
        }
        customer.setCustomerType(customerType.getCode());
        customerDao.update(customer);
    }

    private Customer findCustomerById(Long userId) {
        return customerDao.findCustomerById(userId);
    }

    @Override
    public ApiResult<NullObject> updateCustomerSimple(CustomerRemarksDto customerInfo) {
        customerInfo.check();
        Customer customer = findCustomerById(customerInfo.getCustomerId());
        if (customer == null) {
            throw new IllegalStateException("未查到对应的用户信息");
        }
        customer.setIntention(customerInfo.getIntention());
        customer.setRemarks(customerInfo.getRemarks());
        customerDao.update(customer);
        return ApiResult.succ();
    }
}
