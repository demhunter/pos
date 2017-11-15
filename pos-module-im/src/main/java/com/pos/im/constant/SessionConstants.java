/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * IM相关配置文件
 *
 * @author wangbing
 * @version 1.0, 2017/9/8
 */
@Component
public class SessionConstants {

    @Value("${app.base.url}")
    private String baseUrl;

    @Value("${im.app.key}")
    private String appKey;

    @Value("${im.app.secret}")
    private String appSecret;

    @Value("${im.token.days}")
    private int tokenDays;

    @Value("${im.user.maxSession}")
    private int userMaxSession;

    @Value("${im.session.maxMember}")
    private int sessionMaxMember;

    @Value("${im.session.inactive.day}")
    private int inactiveSessionDay;

    @Value("${im.netease.accid.prefix}")
    private String neteaseAccidPrefix;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public int getTokenDays() {
        return tokenDays;
    }

    public int getUserMaxSession() {
        return userMaxSession;
    }

    public int getSessionMaxMember() {
        return sessionMaxMember;
    }

    public int getInactiveSessionDay() {
        return inactiveSessionDay;
    }

    public String getNeteaseAccidPrefix() {
        return neteaseAccidPrefix;
    }
}
