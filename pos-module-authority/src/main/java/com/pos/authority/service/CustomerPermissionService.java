/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service;

/**
 * 客户权限Service
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public interface CustomerPermissionService {

    /**
     * 初始化客户权限和上下级关联关系
     *
     * @param userId       新注册的用户id
     * @param parentUserId 新注册用户的上级用户id
     */
    void initializePermissionAndRelation(Long userId, Long parentUserId);
}
