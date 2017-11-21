/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.session;

import com.google.common.base.Strings;
import com.pos.common.util.web.access.PermissionValidator;
import com.pos.common.util.web.access.TokenExpiredException;
import com.pos.common.util.web.access.TokenLoggedOtherException;
import com.pos.common.util.web.access.TokenRefreshedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;

import static com.pos.user.session.UserSessionComponent.ACCESS_TOKEN_KEY;
import static com.pos.user.session.UserSessionComponent.SESSION_ID_KEY;

/**
 * 对自定义用户Session进行访问验证的Validator.
 *
 * @author wayne
 * @version 1.0, 2017/2/10
 */
public class UserSessionPermissionValidator implements PermissionValidator {

    static final Logger LOG = LoggerFactory.getLogger(UserSessionPermissionValidator.class);

    private RedisTemplate<String, UserSession> redisTemplate;

    private boolean httpSessionEnable;

    public UserSessionPermissionValidator(RedisTemplate<String, UserSession> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isValidated(HttpServletRequest request) throws TokenExpiredException, TokenRefreshedException, TokenLoggedOtherException {
        String sessionId = getSessionId(request);
        String accessToken = getAccessToken(request);
        if (!Strings.isNullOrEmpty(sessionId) && !Strings.isNullOrEmpty(accessToken)) {
            UserSession session = redisTemplate.opsForValue().get(sessionId);
            if (session != null) {
                if (accessToken.equals(session.getAccessToken())) {
                    if (!session.isAccessTokenExpired()) {
                        return true;
                    } else {
                        throw new TokenExpiredException(
                                "访问令牌已过期！sessionId = " + sessionId + ", accessToken = " + accessToken);
                    }
                } else if (accessToken.equals(session.getOldAccessToken())) {
                    throw new TokenRefreshedException(
                            "访问令牌被刷新，请使用新的令牌！sessionId = " + sessionId + ", accessToken = " + accessToken);
                } else {
                    throw new TokenLoggedOtherException("访问令牌失效，账号在其它设备登录！sessionId=" + sessionId);
                }
            }
        }
        return false;
    }

    public boolean isHttpSessionEnable() {
        return httpSessionEnable;
    }

    public void setHttpSessionEnable(boolean httpSessionEnable) {
        this.httpSessionEnable = httpSessionEnable;
    }

    private String getSessionId(HttpServletRequest request) {
        String sessionId = request.getHeader(SESSION_ID_KEY);
        if (sessionId == null && httpSessionEnable) {
            sessionId = (String) request.getSession().getAttribute(SESSION_ID_KEY);
            if (sessionId != null) {
                LOG.info("从HttpSession中获取到SessionId: " + sessionId);
            }
        }
        return sessionId;
    }

    private String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(ACCESS_TOKEN_KEY);
        if (accessToken == null && httpSessionEnable) {
            accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN_KEY);
            if (accessToken != null) {
                LOG.info("从HttpSession中获取到AccessToken: " + accessToken);
            }
        }
        return accessToken;
    }

}