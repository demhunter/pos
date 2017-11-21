/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.common.sms.constant.MemcachedPrefixType;
import com.pos.common.util.basic.AddressUtils;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.cache.MemcachedClientUtils;
import com.pos.common.util.exception.ErrorCode;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.security.MD5Utils;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
import com.pos.user.constant.UserType;
import com.pos.user.dao.UserClassDao;
import com.pos.user.dao.UserDao;
import com.pos.user.domain.User;
import com.pos.user.domain.UserClass;
import com.pos.user.dto.LoginInfoDto;
import com.pos.user.dto.UserDto;
import com.pos.user.dto.UserLoginDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.LoginService;
import com.pos.user.service.support.UserServiceSupport;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    private UserClassDao userClassDao;

    @Resource
    private UserServiceSupport userServiceSupport;

    @Resource
    private MemcachedClientUtils memcachedClientUtils;

    @Value("${random.token.size}")
    private String randTokenSize;

    @Value("${cache.expire.token.seconds}")
    private String tokenExpireSeconds;

    /**
     * 用户登录验证.
     *
     * @param userLoginDto 用户登录信息
     * @param loginInfoDto 用户登录拓展信息，v1.3.1 主要为IP和设备相关信息
     * @return data根据登录类型手动转型为具体类型
     */
    @Override
    public ApiResult<? extends UserDto> login(UserLoginDto userLoginDto, LoginInfoDto loginInfoDto) {
        checkUserLoginBefore(userLoginDto);

        User user;
        // 登录类型决定是按用户名还是手机号登录
        if (userLoginDto.getUserType().isUsePhone()) {
            user = userDao.getByUserPhone(userLoginDto.getLoginName());
        } else {
            user = userDao.getByUserName(userLoginDto.getLoginName());
        }

        if (user != null && user.isDeleted()) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        } else if (user == null || !user.getPassword().equals(
                MD5Utils.getMD5Code(userLoginDto.getPassword()))) {
            return ApiResult.fail(UserErrorCode.USER_OR_PWD_ERROR);
        }
        // 查询并判断用户是否开通指定类型的账号
        UserClass uc = userClassDao.findClass(user.getId(), userLoginDto.getUserType().getValue());
        ErrorCode err = checkUserLoginAfter(uc);
        if (err != null) {
            // 首次登录时需要返回token以确认请求者身份
            ApiResult<? extends UserDto> failResult = ApiResult.fail(err);
            String token = RandomStringUtils.randomAlphanumeric(Integer.valueOf(randTokenSize));
            if (err.equals(UserErrorCode.FIRST_LOGIN_VERIFY)) {
                failResult.setMessage(token);
            }

            // 将token存储进cache
            memcachedClientUtils.setCacheValue(
                    MemcachedPrefixType.FIRST_LOGIN_TOKEN + userLoginDto.getLoginName(),
                    Integer.valueOf(tokenExpireSeconds),
                    token);

            return failResult;
        }

        UserDto userDto = userServiceSupport.findAndFillUserDto(user, uc);
        if (userDto == null) {
            log.error("没有找到用户数据！userId = " + user.getId() + ", userType = " + uc.getUserType());
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        Date now = Calendar.getInstance().getTime();
        // 记录最近一次登录信息
        userDto.setLastLoginTime(now);
        uc.setLastLoginTime(now);
        uc.setUpdateTime(now);
        // 更新登录信息
        updateLastLogin(uc, loginInfoDto);

        return ApiResult.succ(userDto);
    }

    private void checkUserLoginBefore(UserLoginDto userLoginDto) {
        FieldChecker.checkEmpty(userLoginDto, "userLoginDto");
        FieldChecker.checkEmpty(userLoginDto.getUserType(), "userLoginDto.userType");

        if (userLoginDto.getUserType().isUsePhone()) {
            Validator.checkMobileNumber(userLoginDto.getLoginName());
        } else {
            Validator.checkUserName(userLoginDto.getLoginName());
        }

        Validator.checkPassword(userLoginDto.getPassword());
        if (userLoginDto.getLoginName().equals(userLoginDto.getPassword())) {
            throw new ValidationException(UserErrorCode.USER_PWD_DUPLICATE.getMessage());
        }
    }

    private ErrorCode checkUserLoginAfter(UserClass uc) {
        if (uc == null) {
            return UserErrorCode.USER_NOT_EXISTED;
        }
        if (!uc.isAvailable()) {
            return UserErrorCode.USER_NOT_AVAILABLE;
        }

        // B端用户首次登录时，通知客户端要求其重设密码后再登录
        if (UserType.BUSINESS.getValue().equals(uc.getUserType())) {
            if (uc.getLastLoginTime() == null) {
                return UserErrorCode.FIRST_LOGIN_VERIFY;
            }
        }

        return null;
    }

    /**
     * 更新最近一次登录信息
     * <p>更新最近一次登录信息，v1.3.1 B端和M端不变，只更新最近一次登录时间,C端和E端需要更新登录的IP和登录的设备信息，</p>
     * <p>由于E端用户由B端注册，所以默认E端用户第一次登录的设备信息为注册的设备信息</p>
     * <p>E端和C端用户老数据，默认更新过后第一次登录的设备信息为他的注册设备信息</p>
     *
     * @param uc 旧用户登录信息
     * @param loginInfoDto 登录设备信息
     */
    private void updateLastLogin(UserClass uc, LoginInfoDto loginInfoDto) {
        if ((uc.getUserType().equals(UserType.EMPLOYEE.getValue())
                || uc.getUserType().equals(UserType.CUSTOMER.getValue())) && uc.getRegisterInfo() == null) {
            if (loginInfoDto != null) {
                if (loginInfoDto.getUserExtensionInfo() != null) {
                    uc.setRegisterInfo(JsonUtils.objectToJson(loginInfoDto.getUserExtensionInfo()));
                }
                if (uc.getRegisterIp() == null) {
                    uc.setRegisterIp(loginInfoDto.getIp());
                    uc.setRegisterAddress(AddressUtils.getAddresses(loginInfoDto.getIp()));
                }
            }
        }
        if (loginInfoDto != null) {
            uc.setLastLoginIp(loginInfoDto.getIp());
            uc.setLoginAddress(AddressUtils.getAddresses(loginInfoDto.getIp()));
            if (loginInfoDto.getUserExtensionInfo() != null) {
                uc.setLastLoginInfo(JsonUtils.objectToJson(loginInfoDto.getUserExtensionInfo()));
            }
        }

        // 只能根据userClass.id来更新用户最近一次登录信息记录
        userClassDao.updateLoginInfo(uc);
    }
}