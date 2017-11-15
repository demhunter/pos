/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.http;

import com.google.common.base.Strings;
import com.pos.common.util.basic.JsonUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Cookie工具类.
 *
 * @author wayne
 * @version 1.0, 2016/6/22
 */
public class CookieUtils {

    private CookieUtils() {
    }

    /**
     * 获取当前request请求的所有cookies.
     *
     * @param request HttpServletRequest
     */
    public static Cookie[] getCookies(HttpServletRequest request) {
        return request != null ? request.getCookies() : null;
    }

    /**
     * 获取当前request请求的所有cookies并转为JSON返回.
     *
     * @param request HttpServletRequest
     */
    public static String getCookies2Json(HttpServletRequest request) {
        Cookie[] cookies = getCookies(request);
        if (cookies != null) {
            Map<String, String> cookiesMap = new HashMap<>(cookies.length);
            for (Cookie cookie : cookies) {
                cookiesMap.put(cookie.getName(), cookie.getValue());
            }
            return JsonUtils.objectToJson(cookies);
        } else {
            return null;
        }
    }

    /**
     * 根据cookie名字获取指定cookie.
     *
     * @param request HttpServletRequest
     * @param name cookie name
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        Cookie[] cookies = getCookies(request);
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 根据cookie名字获取指定cookie的值.
     *
     * @param request HttpServletRequest
     * @param name cookie name
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * 将cookie写入客户端.
     *
     * @param response HttpServletResponse
     * @param cookie 要写入的cookie
     */
    public static void writeCookie(HttpServletResponse response, Cookie cookie) {
        if (response != null) {
            response.addCookie(cookie);
        }
    }

    /**
     * 将cookie从客户端删除.
     *
     * @param response HttpServletResponse
     * @param cookie 要删除的cookie
     */
    public static void removeCookie(HttpServletResponse response, Cookie cookie) {
        if (cookie != null) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 将cookie从客户端删除.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param name cookie name
     * @param domain cookie domain
     * @param path cookie path
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response,
                                    String name, String domain, String path) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            cookie.setDomain(domain);
            cookie.setPath(path);
            removeCookie(response, cookie);
        }
    }
}