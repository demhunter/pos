/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.session;

import com.google.common.base.Strings;
import com.pos.common.util.basic.UUIDUnsigned32;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.security.MD5Utils;
import com.pos.common.util.validation.FieldChecker;
import com.pos.user.constant.UserType;
import com.pos.user.exception.UserErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

/**
 * 自定义用户Session的管理组件.
 *
 * @author wayne
 * @version 1.0, 2017/2/10
 */
@Component
public class UserSessionComponent {

    public static final String SESSION_ID_KEY = "session-id";

    public static final String ACCESS_TOKEN_KEY = "access-token";

    static final Logger LOG = LoggerFactory.getLogger(UserSessionComponent.class);

    @Resource
    private RedisTemplate<String, UserSession> defaultRedis;

    @Value("${user.session.httpSessionEnable}")
    private String httpSessionEnable;

    public UserSession add(HttpSession httpSession, UserInfo userInfo) {
        return addOrUpdate(httpSession, userInfo, null);
    }

    public boolean refreshUserInfo(HttpSession httpSession, UserInfo userInfo) {
        UserSession session = defaultRedis.opsForValue().get(getSessionKey(userInfo));
        if (session == null) {
            LOG.warn("刷新用户会话信息失败！cause: 用户不存在. userInfo = {}", userInfo);
            return false;
        }

        if (!Strings.isNullOrEmpty(userInfo.getShowName())) {
            session.getUserInfo().setShowName(userInfo.getShowName());
        }
        if (!Strings.isNullOrEmpty(userInfo.getShowHead())) {
            session.getUserInfo().setShowHead(userInfo.getShowHead());
        }
        defaultRedis.opsForValue().set(session.getSessionId(), session);
        return true;
    }

    public ApiResult<UserSession> update(HttpSession httpSession, String sessionId, String refreshToken) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(refreshToken, "refreshToken");
        UserSession session = defaultRedis.opsForValue().get(sessionId);
        if (session == null) {
            LOG.debug("更新AccessToken失败！cause: 用户不存在." +
                    " sessionId = {}, refreshToken = {}", sessionId, refreshToken);
            return ApiResult.fail(UserErrorCode.USER_NOT_LOGIN);
        }
        if (!session.isAccessTokenExpired()) {
            LOG.debug("更新AccessToken失败！cause: 未到刷新时间." +
                    " sessionId = {}, refreshToken = {}", sessionId, refreshToken);
            return ApiResult.fail(UserErrorCode.USER_TOKEN_CANT_REFRESH);
        }
        if (!session.getRefreshToken().equals(refreshToken)) {
            LOG.debug("更新AccessToken失败！cause: 刷新令牌错误." +
                    " sessionId = {}, refreshToken = {}", sessionId, refreshToken);
            return ApiResult.fail(UserErrorCode.USER_NOT_LOGIN);
        }
        return ApiResult.succ(addOrUpdate(httpSession, session.getUserInfo(), session));
    }

    public void remove(HttpSession httpSession, UserInfo userInfo) {
        if (userInfo != null) {
            defaultRedis.delete(getSessionKey(userInfo));
            if (isHttpSessionEnable()) {
                httpSession.removeAttribute(SESSION_ID_KEY);
                httpSession.removeAttribute(ACCESS_TOKEN_KEY);
            }
        }
    }

    private String getSessionKey(UserInfo userInfo) {
        return userInfo.getUserType() + userInfo.getId();
    }

    private boolean isHttpSessionEnable() {
        return Boolean.TRUE.toString().equalsIgnoreCase(httpSessionEnable);
    }

    private UserSession addOrUpdate(HttpSession httpSession, UserInfo userInfo, UserSession oldSession) {
        UserSession session = new UserSession();
        session.setSessionId(getSessionKey(userInfo));
        session.setAccessToken(MD5Utils.getMD5Code(
                userInfo.getUserType() + userInfo.getId() + UUIDUnsigned32.randomUUIDString()));
        session.setRefreshToken(MD5Utils.getMD5Code(
                userInfo.getId() + userInfo.getUserType() + UUIDUnsigned32.randomUUIDString()));
        session.setUserInfo(userInfo);
        if (UserSession.ACCESS_TOKEN_EXPIRE_TIME > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, UserSession.ACCESS_TOKEN_EXPIRE_TIME);
            session.setExpireTime(cal.getTime());
        }
        if (oldSession != null) {
            session.setOldAccessToken(oldSession.getAccessToken());
        }
        defaultRedis.opsForValue().set(session.getSessionId(), session);

        if (isHttpSessionEnable()) {
            LOG.info("将用户会话写入HttpSession.");
            httpSession.setAttribute(SESSION_ID_KEY, session.getSessionId());
            httpSession.setAttribute(ACCESS_TOKEN_KEY, session.getAccessToken());
        }

        return session;
    }

    /**
     * IM更换服务商之后，已登录用户获取token时报accid为空，在会获取token时为imUid为空的用户更新imUid
     *
     * @param userInfo 新的用户信息
     */
    public void updateUserInfo(UserInfo userInfo) {
        UserSession session = defaultRedis.opsForValue().get(getSessionKey(userInfo));
        if (session != null) {
            session.setUserInfo(userInfo);
            defaultRedis.opsForValue().set(session.getSessionId(), session);
        }
    }


}