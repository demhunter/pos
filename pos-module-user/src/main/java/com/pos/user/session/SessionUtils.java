/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.session;

import com.pos.common.util.cache.MemcachedClientUtils;
import com.pos.user.constant.UserTicket;
import com.pos.user.dto.v1_0_0.UserDto;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Session工具类（不提供获取方法, 通过@FromSession自动注入）.
 *
 * @author wayne
 * @version 1.0, 2016/8/5
 */
public final class SessionUtils {

    public final static String USER_SESSION_KEY = UserInfo.class.getName();

    public static void addUserInfo(HttpSession httpSession, UserDto userDto) {
        httpSession.setAttribute(USER_SESSION_KEY, new UserInfo(userDto));
    }

    public static void removeUserInfo(HttpSession httpSession) {
        httpSession.removeAttribute(USER_SESSION_KEY);
    }

    public static String getTicket(MemcachedClientUtils memcachedClientUtils, Long userId, UserTicket ticketType) {
        if (userId == null || ticketType == null) {
            return null;
        } else {
            return (String) memcachedClientUtils.getCacheNoDeletion(UserTicket.ACTIVITY_COUPON + String.valueOf(userId));
        }
    }

    public static String buildTicket(MemcachedClientUtils memcachedClientUtils, Long userId, UserTicket ticketType) {
        if (userId == null || ticketType == null) {
            return null;
        }
        String ticket = UUID.randomUUID().toString().replace("-", "");

        //todo 增加超时判断，避免造成阻塞
        memcachedClientUtils.setCacheValue(
                UserTicket.ACTIVITY_COUPON + String.valueOf(userId),
                UserTicket.ACTIVITY_COUPON.getExpiration(),
                ticket);

        return ticket;
    }

}