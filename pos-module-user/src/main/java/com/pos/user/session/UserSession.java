/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 自定义的用户会话对象, 包括令牌和用户信息, 用于替代HTTP Session.
 *
 * @author wayne
 * @version 1.0, 2017/2/10
 */
@ApiModel
public class UserSession implements Serializable {

    private static final long serialVersionUID = -248979795394425685L;

    /**
     * 访问令牌的过期时间（分钟）
     */
    public static final int ACCESS_TOKEN_EXPIRE_TIME = -1;

    @ApiModelProperty("会话标识")
    private String sessionId;

    @ApiModelProperty("访问令牌")
    private String accessToken;

    @ApiModelProperty("刷新令牌，只能使用一次")
    private String refreshToken;

    /**
     * 刷新前的accessToken
     */
    @JsonIgnore
    private String oldAccessToken;

    /**
     * accessToken过期时间
     */
    @JsonIgnore
    private Date expireTime;

    /**
     * 记录用户登录会话信息
     */
    @JsonIgnore
    private UserInfo userInfo;

    /**
     * 判断访问令牌是否已经过期.
     *
     * @return 过期返回true, 否则返回false
     */
    public boolean isAccessTokenExpired() {
        return expireTime != null ? new Date(System.currentTimeMillis()).compareTo(expireTime) >= 0 : false;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOldAccessToken() {
        return oldAccessToken;
    }

    public void setOldAccessToken(String oldAccessToken) {
        this.oldAccessToken = oldAccessToken;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}