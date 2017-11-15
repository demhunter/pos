/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import com.pos.im.dao.CommonLanguageDao;
import com.pos.im.dao.UserInfoDao;
import com.pos.im.dao.UserTokenDao;
import com.pos.im.domain.CommonLanguage;
import com.pos.im.domain.UserInfo;
import com.pos.im.domain.UserToken;
import com.pos.im.domain.UserTokenError;
import com.pos.im.dto.user.*;
import com.pos.im.service.IMUserService;
import com.pos.im.service.support.IMClient;
import com.pos.im.dao.UserTokenErrorDao;
import com.pos.im.exception.IMServerException;
import com.pos.im.service.support.NoticeTemplate;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IM用户服务实现类.
 *
 * @author wayne
 * @version 1.0, 2016/7/7
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IMUserServiceImpl implements IMUserService {

    private Logger logger = LoggerFactory.getLogger(IMUserServiceImpl.class);

    private static final Object INIT_LANGUAGE_LOCK = new Object();

    @Resource
    private IMClient apiClientConfig;

    @Resource
    private NoticeTemplate noticeTemplate;

    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private UserTokenDao userTokenDao;

    @Resource
    private UserTokenErrorDao userTokenErrorDao;

    @Resource
    private CommonLanguageDao commonLanguageDao;

    @Override
    public ApiResult<UserTokenDto> getToken(UserInfoDto userInfo) throws IMServerException {
        FieldChecker.checkEmpty(userInfo, "userInfo");
        userInfo.check("userInfoDto");

        // 用户已经申请过token且没有过期则直接返回token
        UserToken userToken = userTokenDao.findUserToken(
                userInfo.getUserId(), userInfo.getUserType());
        if (userToken != null && userToken.isAvailable() && !isTokenExpired(userToken)) {
            logger.info("用户({})的IM Token已存在. token = {}", userInfo.getImUid(), userToken);
            UserTokenDto userTokenDto = convert2UserTokenDto(userToken);
            userTokenDto.setImUid(userInfo.getImUid());
            return ApiResult.succ(userTokenDto);
        }

        // 用户没有申请过token或已经过期则申请新的token
        String token = getNewToken(userInfo.getImUid());
        if (userToken == null) {
            logger.info("新增用户({})的IM Token.", userInfo.getImUid());
            userToken = new UserToken();
            userToken.setUserId(userInfo.getUserId());
            userToken.setUserType(userInfo.getUserType());
            userToken.setToken(token);
            userToken.setAvailable(true);
            userToken.setCreateTime(Calendar.getInstance().getTime());
            userTokenDao.save(userToken);
        } else {
            logger.info("更新用户({})的IM Token. token = {}", userInfo.getImUid(), token);
            userToken.setToken(token);
            userToken.setAvailable(true);
            userToken.setCreateTime(Calendar.getInstance().getTime());
            userTokenDao.update(userToken);
        }

        UserTokenDto userTokenDto = convert2UserTokenDto(userToken);
        userTokenDto.setImUid(userInfo.getImUid());
        return ApiResult.succ(userTokenDto);
    }

    @Override
    public void addTokenError(UserIdentifier user, UserTokenErrorDto userTokenErrorDto) {
        logger.info("记录APP获取TOKEN的错误信息：" + userTokenErrorDto);
        UserTokenError error = new UserTokenError();
        error.setUserId(user.getUserId());
        error.setUserType(user.getUserType());
        error.setImKey(userTokenErrorDto.getImKey());
        error.setImUserId(userTokenErrorDto.getImUserId());
        error.setImToken(userTokenErrorDto.getImToken());
        error.setMessage(userTokenErrorDto.getMessage());
        error.setCreateTime(Calendar.getInstance().getTime());
        userTokenErrorDao.save(error);
    }

    @Override
    public void createIMUser(UserInfoDto userInfo) throws IMServerException {
        FieldChecker.checkEmpty(userInfo, "userInfo");
        userInfo.check("userInfo");

        // 构建imUid，保存im_user_info
        userInfo.setImUid(apiClientConfig.formatAccid(userInfo.buildUserIdentifier()));
        UserInfo persistent = convert2UserInfo(userInfo);
        persistent.setCreateTime(Calendar.getInstance().getTime());
        persistent.setUpdateTime(persistent.getCreateTime());
        userInfoDao.save(persistent);

        // 注册IM账号，保存用户IM Token
        String token = apiClientConfig.createUser(userInfo);
        logger.info("新增用户({})的IM Token.", userInfo.getImUid());
        UserToken userToken = new UserToken();
        userToken.setUserId(userInfo.getUserId());
        userToken.setUserType(userInfo.getUserType());
        userToken.setToken(token);
        userToken.setAvailable(true);
        userToken.setCreateTime(Calendar.getInstance().getTime());
        userTokenDao.save(userToken);
    }

    @Override
    public void updateIMUser(UserInfoDto userInfo) throws IMServerException {
        FieldChecker.checkEmpty(userInfo, "userInfo");
        userInfo.check("userInfo");

        userInfo.setImUid(apiClientConfig.formatAccid(userInfo.buildUserIdentifier()));
        apiClientConfig.updateUser(userInfo);
        UserInfo persistent = convert2UserInfo(userInfo);
        persistent.setUpdateTime(new Date());
        userInfoDao.updateByUser(persistent);
    }

    @Override
    public UserInfoDto findUser(Long userId, String userType, boolean disable) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(userType, "userType");
        return convert2UserInfoDto(userInfoDao.find(userId, userType, disable ? null : true));
    }

    @Override
    public ApiResult<UserGreetingDto> getUserGreetingLanguage(UserIdentifier user) {
        FieldChecker.checkEmpty(user, "user");
        user.check("user");
        UserInfo userInfo = userInfoDao.find(user.getUserId(), user.getUserType(), true);
        if (userInfo == null) {
            return ApiResult.fail(CommonErrorCode.DATA_NOT_FOUND);
        } else if (userInfo.getUserDetailType() != null
                && userInfo.getUserDetailType() != IMUserService.PLATFORM_EMPLOYEE_TYPE) {
            return ApiResult.fail(CommonErrorCode.NO_PERMISSIONS);
        } else {
            return ApiResult.succ(new UserGreetingDto(
                    noticeTemplate.formatSessionDefaultGreeting(userInfo.getShowName()), userInfo.getAppendGreeting()));
        }
    }

    @Override
    public ApiResult<NullObject> updateUserGreetingLanguage(UserIdentifier user, String appendGreeting) {
        FieldChecker.checkEmpty(user, "user");
        user.check("user");
        UserInfo userInfo = userInfoDao.find(user.getUserId(), user.getUserType(), true);
        if (userInfo == null) {
            return ApiResult.fail(CommonErrorCode.DATA_NOT_FOUND);
        }
        if (userInfo.getUserDetailType() != null
                && userInfo.getUserDetailType() != IMUserService.PLATFORM_EMPLOYEE_TYPE) {
            return ApiResult.fail(CommonErrorCode.NO_PERMISSIONS);
        }
        userInfo.setAppendGreeting(Strings.nullToEmpty(appendGreeting));
        if (userInfo.getAppendGreeting().length() > 60) {
            throw new ValidationException("问候语最多60个字！");
        }
        userInfoDao.update(userInfo);
        return ApiResult.succ();
    }

    @Override
    public List<UserLanguageDto> getUserCommonLanguages(UserIdentifier user) {
        FieldChecker.checkEmpty(user, "user");
        user.check("user");

        List<CommonLanguage> languages = commonLanguageDao.findLanguagesByUser(user);
        if (CollectionUtils.isEmpty(languages)) {
            synchronized (INIT_LANGUAGE_LOCK) {
                if (CollectionUtils.isEmpty(commonLanguageDao.findLanguagesByUser(user))) {
                    languages = initUserCommonLanguages(user);
                }
            }
        }

        List<UserLanguageDto> userLanguageDtos = languages.stream()
                .filter(cl -> Boolean.TRUE.equals(cl.getAvailable()))
                .map(cl -> new UserLanguageDto(cl.getId(), cl.getContent()))
                .collect(Collectors.toList());
        return userLanguageDtos;
    }

    @Override
    public ApiResult<UserLanguageDto> addUserCommonLanguage(UserIdentifier user, String content) {
        FieldChecker.checkEmpty(user, "user");
        user.check("user");
        FieldChecker.checkEmpty(content, "content");
        if (content.length() > 60) {
            throw new ValidationException("回复语最多60个字！");
        }

        CommonLanguage newLanguage = null;
        List<CommonLanguage> languages = commonLanguageDao.findLanguagesByUser(user);
        if (!CollectionUtils.isEmpty(languages)) {
            if (languages.stream()
                    .filter(cl -> Boolean.TRUE.equals(cl.getAvailable())).count() >= 10) {
                throw new ValidationException("请先删除一些常用回复后才能继续添加！");
            }

            List<CommonLanguage> delLanguages = languages.stream()
                    .filter(cl -> Boolean.FALSE.equals(cl.getAvailable())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(delLanguages)) {
                newLanguage = delLanguages.get(0);
                newLanguage.setContent(content);
                newLanguage.setAvailable(Boolean.TRUE);
                newLanguage.setUpdateTime(Calendar.getInstance().getTime());
                commonLanguageDao.update(newLanguage);
            }
        }

        if (newLanguage == null) {
            newLanguage = saveUserCommonLanguage(user, content);
        }

        return ApiResult.succ(new UserLanguageDto(newLanguage.getId(), newLanguage.getContent()));
    }

    @Override
    public ApiResult<NullObject> delUserCommonLanguage(UserIdentifier user, Long languageId) {
        FieldChecker.checkEmpty(user, "user");
        user.check("user");
        FieldChecker.checkEmpty(languageId, "languageId");

        CommonLanguage cl = commonLanguageDao.get(languageId);
        if (cl == null || !cl.isOwner(user)) {
            return ApiResult.fail(CommonErrorCode.DATA_NOT_FOUND);
        }
        if (Boolean.TRUE.equals(cl.getAvailable())) {
            cl.setAvailable(Boolean.FALSE);
            cl.setUpdateTime(Calendar.getInstance().getTime());
            commonLanguageDao.update(cl);
        }
        return ApiResult.succ();
    }

    @Override
    public String buildNeteaseIMAccid(UserIdentifier user) {
        return apiClientConfig.formatAccid(user);
    }

    private UserTokenDto convert2UserTokenDto(UserToken userToken) {
        return new UserTokenDto(userToken.getUserId(),
                userToken.getUserType(), userToken.getToken());
    }

    @SuppressWarnings("all")
    private UserInfoDto convert2UserInfoDto(UserInfo userInfo) {
        if (userInfo != null) {
            UserInfoDto userInfoDto = new UserInfoDto();
            BeanUtils.copyProperties(userInfo, userInfoDto);
            return userInfoDto;
        } else {
            return null;
        }
    }

    private UserInfo convert2UserInfo(UserInfoDto userInfoDto) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoDto, userInfo);
        return userInfo;
    }

    @SuppressWarnings("all")
    private boolean isTokenExpired(UserToken userToken) {
        if (apiClientConfig.getTokenDays() > 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(userToken.getCreateTime());
            cal.add(Calendar.MINUTE, -10); // Token到期差10分钟即可重新获取
            return Days.daysBetween(new DateTime(
                    cal.getTime()), new DateTime()).getDays() >= apiClientConfig.getTokenDays();
        } else {
            return false;
        }
    }

    /**
     * 获取用户最新IM Token信息
     *
     * @param imUid 用户信息
     * @return IM Token
     * @throws IMServerException 获取IM Token异常
     */
    private String getNewToken(String imUid) throws IMServerException {
        return apiClientConfig.refreshToken(imUid);
    }

    private CommonLanguage saveUserCommonLanguage(UserIdentifier user, String content) {
        CommonLanguage newLanguage = new CommonLanguage();
        newLanguage.setUserId(user.getUserId());
        newLanguage.setUserType(user.getUserType());
        newLanguage.setContent(content);
        newLanguage.setAvailable(Boolean.TRUE);
        newLanguage.setCreateTime(Calendar.getInstance().getTime());
        newLanguage.setUpdateTime(newLanguage.getCreateTime());
        commonLanguageDao.save(newLanguage);
        return newLanguage;
    }

    private List<CommonLanguage> initUserCommonLanguages(UserIdentifier user) {
        String[] defaultLanguages = null;
        if (IMUserService.USER_TYPE_CUSTOMER.equals(user.getUserType())) {
            defaultLanguages = noticeTemplate.getSessionCustomerDefaultLanguages();
        } else if (IMUserService.USER_TYPE_EMPLOYEE.equals(user.getUserType())) {
            defaultLanguages = noticeTemplate.getSessionEmployeeDefaultLanguages();
        }

        if (defaultLanguages != null && defaultLanguages.length > 0) {
            List<CommonLanguage> list = new ArrayList<>(defaultLanguages.length);
            for (String content : defaultLanguages) {
                list.add(saveUserCommonLanguage(user, content));
            }
            return list;
        } else {
            return Lists.newArrayListWithCapacity(0);
        }
    }

}