/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.google.common.base.Strings;
import com.pos.common.sms.constant.MemcachedPrefixType;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.security.MD5Utils;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
import com.pos.user.condition.query.UserListCondition;
import com.pos.user.constant.UserType;
import com.pos.user.dao.UserDao;
import com.pos.user.domain.User;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户基础服务接口类实现
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Resource
    private SmsService smsService;

    @Resource
    private UserDao userDao;

    @Override
    public ApiResult<NullObject> updatePwdByPhone(
            String userPhone, String newPwd, String smsCode, UserType userType) {
        FieldChecker.checkEmpty(smsCode, "smsCode");
        FieldChecker.checkEmpty(userType, "userType");
        Validator.checkMobileNumber(userPhone);
        Validator.checkPassword(newPwd);

        User user = userDao.getByPhoneAndType(userPhone, userType.getValue());
        if (user == null || !user.isEnable()) {
            if (UserType.CUSTOMER.equals(userType)) {
                return ApiResult.failFormatMsg(UserErrorCode.USER_NOT_REGISTERED_C, userPhone);
            } else {
                throw new IllegalStateException("非法的用户类型！");
            }
        }

        if (!smsService.checkVerifyCode(
                userPhone, smsCode, MemcachedPrefixType.RETRIEVE).isSucc()) {
            return ApiResult.fail(CommonErrorCode.VERIFY_CODE_ERROR);
        }

        // 新旧密码不相同时才执行更新
        String newPwdMd5 = MD5Utils.getMD5Code(newPwd);
        if (!user.getPassword().equals(newPwdMd5)) {
            user.setPassword(newPwdMd5);
            userDao.update(user);
        }
        return ApiResult.succ();
    }

    /**
     * 查询符合queryKey的用户id列表
     *
     * @param searchKey 搜索关键字
     * @return 用户ID列表
     */
    @Override
    public List<Long> queryCustomerUserIds(String searchKey) {
        UserListCondition condition = new UserListCondition();
        if (!Strings.isNullOrEmpty(searchKey)) {
            if (SimpleRegexUtils.isMobile(searchKey)) {
                // 输入的搜索关键字是手机号
                condition.setUserPhone(searchKey);
            } else {
                condition.setSearchKey(searchKey);
            }
        }
        return userDao.queryCustomerUserIds(condition);
    }
}
