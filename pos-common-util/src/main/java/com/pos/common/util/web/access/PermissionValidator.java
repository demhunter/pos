/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.access;

import javax.servlet.http.HttpServletRequest;

/**
 * WEB访问权限的验证器接口.
 *
 * @author wayne
 * @version 1.0, 2016/6/21
 */
public interface PermissionValidator {

    /**
     * 是否通过验证.
     *
     * @param request HttpServletRequest
     * @throws TokenExpiredException   可选异常: 通过验证但令牌已过期时抛出此异常, 表示应调用令牌刷新接口
     * @throws TokenRefreshedException 可选异常: 没有通过验证但与刷新前的令牌相符时抛出此异常, 表示应使用刷新后的令牌重新请求
     */
    boolean isValidated(HttpServletRequest request) throws TokenExpiredException, TokenRefreshedException, TokenLoggedOtherException;

}
