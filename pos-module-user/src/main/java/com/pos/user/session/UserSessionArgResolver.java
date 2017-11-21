/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.session;

import com.google.common.base.Strings;
import com.pos.common.util.mvc.resolver.FromSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

import static com.pos.user.session.UserSessionComponent.ACCESS_TOKEN_KEY;
import static com.pos.user.session.UserSessionComponent.SESSION_ID_KEY;

/**
 * Controller的方法参数如果有@FromSession注解, 则根据HTTP请求头获取自定义的用户Session, 并在SpringMVC解析后自动为该参数赋值.
 * <p>
 * 注意: @FromSession注解的参数类型必须是com.ywmj.user.session.UserInfo
 *
 * @author wayne
 * @version 1.0, 2017/2/13
 */
public class UserSessionArgResolver implements HandlerMethodArgumentResolver {

    static final Logger LOG = LoggerFactory.getLogger(UserSessionArgResolver.class);

    private RedisTemplate<String, UserSession> redisTemplate;

    private boolean httpSessionEnable;

    public UserSessionArgResolver(RedisTemplate<String, UserSession> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(FromSession.class) != null
                && parameter.getParameterType().isAssignableFrom(UserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request != null) {
            String sessionId = getSessionId(request);
            String accessToken = getAccessToken(request);
            if (!Strings.isNullOrEmpty(sessionId) && !Strings.isNullOrEmpty(accessToken)) {
                UserSession session = redisTemplate.opsForValue().get(sessionId);
                if (session != null && accessToken.equals(session.getAccessToken()) && !session.isAccessTokenExpired()) {
                    return session.getUserInfo();
                }
            }
        }
        return null;
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