/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
import com.pos.user.constant.UserType;
import com.pos.user.dao.MerchantDao;
import com.pos.user.domain.Merchant;
import com.pos.user.dto.merchant.MerchantDto;
import com.pos.user.dto.converter.UserDtoConverter;
import com.pos.user.service.MerchantService;
import com.pos.user.service.support.UserServiceSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 商家管理员相关服务接口的实现类.
 *
 * @author wayne
 * @version 1.0, 2016/8/4
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MerchantServiceImpl implements MerchantService {

    @Resource
    private MerchantDao merchantDao;

    @Resource
    private UserServiceSupport userServiceSupport;

    @Override
    public MerchantDto findById(Long userId, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(userId, "userId");
        return (MerchantDto) userServiceSupport.findById(userId, UserType.BUSINESS, disable, deleted);
    }

    @Override
    public MerchantDto findByUserName(String userName, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(userName, "userName");
        return (MerchantDto) userServiceSupport.findByUserName(userName, UserType.BUSINESS, disable, deleted);
    }

    @Override
    public MerchantDto findByUserPhone(String userPhone, boolean disable, boolean deleted) {
        Validator.checkMobileNumber(userPhone);
        return (MerchantDto) userServiceSupport.findByUserPhone(userPhone, UserType.BUSINESS, disable, deleted);
    }

    @Override
    public boolean update(MerchantDto beforeDto, MerchantDto afterDto) {
        userServiceSupport.checkUpdate(beforeDto, afterDto);
        boolean isUpdated = userServiceSupport.updateUser(beforeDto, afterDto);
        Merchant before = UserDtoConverter.convert2Merchant(beforeDto);
        Merchant after = UserDtoConverter.convert2Merchant(afterDto);
        if (!JsonUtils.objectToJson(before).equals(JsonUtils.objectToJson(after))) {
            merchantDao.update(after);
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

}