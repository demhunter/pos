/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.common.util.basic.IpAddressUtils;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.security.MD5Utils;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
import com.pos.user.constant.UserType;
import com.pos.user.dao.CustomerDao;
import com.pos.user.dao.ManagerDao;
import com.pos.user.dao.UserDao;
import com.pos.user.domain.User;
import com.pos.user.domain.UserExtension;
import com.pos.user.dto.login.IdentityInfoDto;
import com.pos.user.dto.login.UserExtensionInfoDto;
import com.pos.user.dto.login.LoginInfoDto;
import com.pos.user.dto.UserDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * 用户登录服务实现类.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LoginServiceImpl implements LoginService {

    private final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Resource
    private CustomerDao customerDao;

    @Resource
    private ManagerDao managerDao;

    @Override
    public ApiResult<? extends UserDto> login(LoginInfoDto loginInfo) {
        IdentityInfoDto identityInfo = loginInfo.getIdentityInfoDto();
        // 登录前参数校验
        checkUserLoginBefore(identityInfo);

        User user = userDao.getByLoginName(identityInfo.getLoginName(), identityInfo.getUserType());
        if (user != null && !user.getEnableStatus()) {
            return ApiResult.fail(UserErrorCode.ACCOUNT_DELETED);
        } else if (user == null || !user.getPassword().equals(
                MD5Utils.getMD5Code(identityInfo.getPassword()))) {
            return ApiResult.fail(UserErrorCode.USER_OR_PWD_ERROR);
        }
        UserDto userDto = findAndFillUserDto(user);
        if (userDto == null) {
            log.error("没有找到用户数据！userId = " + user.getId() + ", userType = " + user.getUserType());
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        Date lastLoginTime = Calendar.getInstance().getTime();
        // 更新登录信息
        updateLastLogin(user, loginInfo.getUserExtensionInfo(), lastLoginTime);
        userDto.setLastLoginTime(lastLoginTime);

        return ApiResult.succ(userDto);
    }

    private void checkUserLoginBefore(IdentityInfoDto identityInfo) {
        FieldChecker.checkEmpty(identityInfo, "identityInfo");

        if (UserType.CUSTOMER.equals(UserType.getEnum(identityInfo.getUserType()))) {
            Validator.checkMobileNumber(identityInfo.getLoginName());
        } else {
            Validator.checkUserName(identityInfo.getLoginName());
        }

        Validator.checkPassword(identityInfo.getPassword());
        if (identityInfo.getLoginName().equals(identityInfo.getPassword())) {
            throw new ValidationException(UserErrorCode.USER_PWD_DUPLICATE.getMessage());
        }
    }

    private UserDto findAndFillUserDto(User user) {
        UserType userType = UserType.getEnum(user.getUserType());
        UserDto userDto = null;

        if (UserType.CUSTOMER.equals(userType)) {
            userDto = customerDao.findByUserIdAndEnable(user.getId(), null);
        } else if (UserType.MANAGER.equals(userType)) {
            userDto = managerDao.findByUserIdAndEnable(user.getId(), null);
        }

        return userDto;
    }

    /**
     * 更新最近一次登录信息
     *
     * @param user          用户账号信息
     * @param extensionInfo 用户拓展信息
     */
    private void updateLastLogin(User user, UserExtensionInfoDto extensionInfo, Date lastLoginTime) {
        UserExtension extension = userDao.getExtension(user.getId());
        String extensionStr = null;
        if (extensionInfo != null) {
            if (StringUtils.isNotEmpty(extensionInfo.getIp())) {
                extensionInfo.setIpAddress(IpAddressUtils.getAddresses(extensionInfo.getIp()));
            }
            extensionStr = JsonUtils.objectToJson(extensionInfo);
        }
        if (extension == null) {
            extension = new UserExtension();
            extension.setUserId(user.getId());
            extension.setRegisterTime(lastLoginTime);
            extension.setRegisterInfo(extensionStr);
            extension.setLastLoginTime(lastLoginTime);
            extension.setLastLoginInfo(extensionStr);
            userDao.saveExtension(extension);
        } else {
            extension.setLastLoginTime(lastLoginTime);
            if (extensionStr != null) {
                extension.setLastLoginInfo(JsonUtils.objectToJson(extensionInfo));
            }
            userDao.updateExtension(extension);
        }
    }
}