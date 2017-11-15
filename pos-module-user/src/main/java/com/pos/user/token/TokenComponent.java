/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.token;

import com.pos.common.sms.constant.MemcachedPrefixType;
import com.pos.common.util.basic.UUIDUnsigned32;
import com.pos.common.util.cache.MemcachedClientUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.validation.Preconditions;
import com.pos.user.exception.UserErrorCode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * TokenComponent
 *
 * @author cc
 * @version 1.0, 2017/1/4
 */
@Component
public class TokenComponent {

    @Resource
    private MemcachedClientUtils memcachedClientUtils;

    /**
     * 检查token是否合法（用户是否在登录状态）
     *
     * @param token token
     * @param deleteKey 从Cache中取键之后是否删除键
     * @return 检查结果，成功则返回userId
     */
    public ApiResult<Long> checkLogin(String token, Boolean deleteKey) {
        Preconditions.checkArgsNotNull(token, deleteKey);

        Long userId;
        if (!deleteKey) {
            userId = (Long) memcachedClientUtils.getCacheNoDeletion(MemcachedPrefixType.H5_LOGIN_TOKEN + ":" + token);
            if (userId == null) {
                return ApiResult.fail(UserErrorCode.TOKEN_INVALID);
            }
        } else {
            userId = (Long) memcachedClientUtils.getCacheNoDeletion(MemcachedPrefixType.H5_LOGIN_TOKEN + ":" + token);
            if (userId == null) {
                return ApiResult.fail(UserErrorCode.TOKEN_INVALID);
            }
        }

        return ApiResult.succ(userId);
    }

    /**
     * 获取登录Token（同时将用户信息放入Cache）
     *
     * @param exp 过期时间（秒）
     * @param userId 用户id
     * @return token
     */
    public String acquireLoginToken(int exp, Long userId) {
        Preconditions.checkArgument(exp > 0 && userId != null, "参数不合法！");

        String token = UUIDUnsigned32.randomUUIDString();
        memcachedClientUtils.setCacheValue(MemcachedPrefixType.H5_LOGIN_TOKEN + ":" + token, exp, userId);

        return token;
    }
}
