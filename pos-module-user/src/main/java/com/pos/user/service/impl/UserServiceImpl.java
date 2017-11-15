/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.google.common.base.Strings;
import com.pos.common.util.mvc.support.*;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.sms.constant.MemcachedPrefixType;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.cache.MemcachedClientUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.security.MD5Utils;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Preconditions;
import com.pos.common.util.validation.Validator;
import com.pos.user.constant.UserType;
import com.pos.user.dao.UserClassDao;
import com.pos.user.domain.UserFeedback;
import com.pos.user.dto.converter.UserDtoConverter;
import com.pos.user.dto.feedback.NewFeedbackDto;
import com.pos.user.service.UserService;
import com.pos.user.condition.orderby.UserFeedbackOrderField;
import com.pos.user.condition.query.UserListCondition;
import com.pos.user.dao.UserDao;
import com.pos.user.dao.UserFeedbackDao;
import com.pos.user.domain.User;
import com.pos.user.domain.UserClass;
import com.pos.user.dto.UserAuthInfoDto;
import com.pos.user.dto.UserDto;
import com.pos.user.dto.feedback.UserFeedbackDto;
import com.pos.user.exception.UserErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * 用户基础服务的实现类.
 *
 * @author wayne
 * @version 1.0, 2016/6/8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final static String SYSTEM_DEFAULT_PWD_MD5 = MD5Utils.getMD5Code("ywmj123456");

    @Resource
    private UserDao userDao;

    @Resource
    private UserClassDao userClassDao;

    @Resource
    private UserFeedbackDao userFeedbackDao;

    @Resource
    private SmsService smsService;

    @Resource
    private MemcachedClientUtils memcachedClientUtils;

    @Override
    public void update(UserDto userDto) {
        Preconditions.checkNotNull(userDto, "用户信息不能为空！");
        userDao.update(UserDtoConverter.convert2User(userDto));
    }

    @Override
    public boolean isIdNumberExists(String idNumber) {
        return isIdNumberExists(null, idNumber);
    }

    @Override
    public boolean isIdNumberExists(Long userId, String idNumber) {
        Validator.checkIdNumber(idNumber);
        int count = userDao.countIdNumber(idNumber);
        if (count == 0) {
            return false;
        } else if (count > 1) {
            return true;
        } else {
            if (userId != null) {
                User user = userDao.getById(userId);
                if (user != null && idNumber.equals(user.getIdCard())) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public Long getUserClassId(UserIdentifier user) {
        FieldChecker.checkEmpty(user, "user");
        user.check("user");
        return userClassDao.getClassId(user.getUserId(), user.getUserType());
    }

    @Override
    public UserAuthInfoDto getUserAuthInfo(UserIdentifier user) {
        FieldChecker.checkEmpty(user, "user");
        user.check("user");
        User entity = userDao.getById(user.getUserId());
        if (entity != null) {
            UserAuthInfoDto authInfo = new UserAuthInfoDto();
            authInfo.setUserId(entity.getId());
            authInfo.setUserType(user.getUserType()); // 填充用户类型
            authInfo.setName(entity.getName());
            authInfo.setPhone(entity.getUserPhone());
            authInfo.setGender(entity.getGender());
            authInfo.setAge(entity.getAge());
            authInfo.setMail(entity.getMail());
            authInfo.setIdCard(entity.getIdCard());
            authInfo.setIdImageA(entity.getIdImageA());
            authInfo.setIdImageB(entity.getIdImageB());
            authInfo.setIdHoldImage(entity.getIdHoldImage());
            authInfo.setIdHoldImageB(entity.getIdHoldImageB());
            return authInfo;
        } else {
            return null;
        }
    }

    @Override
    public ApiResult<NullObject> resetPwdById(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");
        User user = userDao.getById(userId);
        if (user == null || user.isDeleted()) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        user.setPassword(SYSTEM_DEFAULT_PWD_MD5);
        userDao.update(user);
        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> updatePwdById(Long userId, String oldPwd, String newPwd) {
        FieldChecker.checkEmpty(userId, "userId");
        Validator.checkPassword(oldPwd);
        Validator.checkPassword(newPwd);

        if (oldPwd.equals(newPwd)) {
            return ApiResult.fail(CommonErrorCode.MODIFY_NOT_CHANGED);
        }

        User user = userDao.getById(userId);
        if (user == null || user.isDeleted()) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        if (!user.getPassword().equals(MD5Utils.getMD5Code(oldPwd))) {
            return ApiResult.fail(UserErrorCode.OLD_PWD_ERROR);
        }

        user.setPassword(MD5Utils.getMD5Code(newPwd));
        userDao.update(user);
        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> updatePwdByPhone(
            String userPhone, String newPwd, String smsCode, UserType userType) {
        FieldChecker.checkEmpty(smsCode, "smsCode");
        Validator.checkMobileNumber(userPhone);
        Validator.checkPassword(newPwd);

        User user = userDao.getByUserPhone(userPhone);
        if (user == null || user.isDeleted()) {
            if (userType.equals(UserType.EMPLOYEE)) {
                return ApiResult.failFormatMsg(UserErrorCode.USER_NOT_REGISTERED_E, userPhone);
            } else if (userType.equals(UserType.CUSTOMER)) {
                return ApiResult.failFormatMsg(UserErrorCode.USER_NOT_REGISTERED_C, userPhone);
            } else {
                throw new IllegalStateException("非法的用户类型！");
            }
        }

        UserClass userClass = userClassDao.findClass(user.getId(), userType.getValue());
        if (userClass == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
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

    @Override
    public ApiResult<NullObject> initPwdByUserName(
            String userName, String newPwd, String token, UserType userType) {
        FieldChecker.checkEmpty(userType, "userType");
        FieldChecker.checkEmpty(token, "token");
        Validator.checkUserName(userName);
        Validator.checkPassword(newPwd);

        String modifyToken = (String) memcachedClientUtils.getCacheValue(
                MemcachedPrefixType.FIRST_LOGIN_TOKEN + userName);
        if (!token.equals(modifyToken)) {
            return ApiResult.fail(UserErrorCode.RESET_PASSWORD_FAILED);
        }

        User user = userDao.getByUserName(userName);
        if (user == null || user.isDeleted()) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        UserClass uc = userClassDao.findClass(user.getId(), userType.getValue());
        if (uc == null || !uc.isAvailable()) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        user.setPassword(MD5Utils.getMD5Code(newPwd));
        userDao.update(user);
        // 修改密码之后的登录，不再是首次登录
        userClassDao.updateLastLoginTime(uc.getId(), Calendar.getInstance().getTime());

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> updateStatus(Long userId, UserType userType, boolean available) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(userType, "userType");

        UserClass uc = userClassDao.findClass(userId, userType.getValue());
        if (uc == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        // 用户状态有修改时才做更新
        if (uc.isAvailable() != available) {
            uc.setAvailable(available);
            uc.setUpdateTime(Calendar.getInstance().getTime());
            userClassDao.update(uc);
        }
        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> addFeedback(Long userId, NewFeedbackDto feedbackDto) {
        FieldChecker.checkEmpty(feedbackDto, "feedbackDto");
        feedbackDto.check("feedbackDto");

        if (userId != null) {
            User user = userDao.getById(userId);
            if (user == null || user.isDeleted()) {
                return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
            }
        }

        UserFeedback uf = new UserFeedback();
        uf.setUserId(userId != null ? userId : -1L); // userId字段不能为空, 未登录用户发表反馈意见统一为-1
        uf.setAppType(feedbackDto.getAppType());
        uf.setAppVersion(feedbackDto.getAppVersion());
        uf.setDeviceId(feedbackDto.getDeviceId());
        uf.setDeviceOS(feedbackDto.getDeviceOS());
        uf.setDeviceModel(feedbackDto.getDeviceModel());
        uf.setContent(feedbackDto.getContent());
        uf.setCreateTime(Calendar.getInstance().getTime());
        if (feedbackDto.getImages() != null && !feedbackDto.getImages().isEmpty()) {
            uf.setImages(String.join(",", feedbackDto.getImages()));
        }
        userFeedbackDao.save(uf);

        return ApiResult.succ();
    }

    @Override
    public Pagination<UserFeedbackDto> findFeedbackList(LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(UserFeedbackOrderField.getInterface());
        }

        int totalCount = userFeedbackDao.getTotal();
        Pagination<UserFeedbackDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            pagination.setResult(userFeedbackDao.findList(limitHelper, orderHelper));
        }

        return pagination;
    }

    /**
     * 查询符合queryKey的用户id列表
     *
     * @param searchKey 搜索关键字
     * @return 用户ID列表
     */
    @SuppressWarnings("all")
    @Override
    public List<Long> queryUserIds(String searchKey) {
        UserListCondition condition = new UserListCondition();
        if (!Strings.isNullOrEmpty(searchKey)) {
            if (SimpleRegexUtils.isMobile(searchKey)) {
                // 输入的搜索关键字是手机号
                condition.setUserPhone(searchKey);
            } else {
                condition.setSearchKey(searchKey);
            }
        }
        return userDao.queryUserIds(condition);
    }
}