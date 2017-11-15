/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.pos.dto.auth.AuthorityDetailDto;
import com.pos.pos.dto.auth.BaseAuthDto;
import com.pos.user.dto.login.RegisterRecommendDto;

/**
 * POS权限控制Service
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
public interface AuthorityService {

    /**
     * 初始收款用户权限信息
     *
     * @param userId    用户id
     * @param recommend 推荐人信息
     */
    void initializeAuthority(Long userId, RegisterRecommendDto recommend);

    /**
     * 更改快捷收款用户权限信息
     *
     * @param authId   被更新用户posID
     * @param baseAuth 新权限信息
     * @param user     操作人
     * @return 操作结果
     */
    ApiResult<NullObject> updateAuthority(Long authId, BaseAuthDto baseAuth, UserIdentifier user);

    /**
     * 获取指定用户的基本权限信息
     *
     * @param posId 主键id
     * @return 基本权限信息
     */
    ApiResult<BaseAuthDto> getBaseAuthById(Long posId);

    /**
     * 获取快捷收款用户详细信息
     *
     * @param userId 用户userId
     * @return 快捷收款用户详细信息
     */
    AuthorityDetailDto findAuthDetail(Long userId);
}
