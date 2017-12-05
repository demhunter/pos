/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service;

import com.pos.authority.dto.level.CustomerLevelConfigDto;
import com.pos.authority.dto.level.CustomerUpgradeLevelDto;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.common.util.mvc.support.ApiResult;

import java.util.List;

/**
 * 客户权限Service
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public interface CustomerAuthorityService {

    /**
     * 初始化客户权限和上下级关联关系
     *
     * @param userId       新注册的用户id
     * @param parentUserId 新注册用户的上级用户id
     */
    void initialize(Long userId, Long parentUserId);

    /**
     * 获取指定用户的权限信息
     *
     * @param userId 用户id
     * @return 权限信息
     */
    CustomerPermissionDto getPermission(Long userId);

    /**
     * 获取等级配置信息列表
     *
     * @param userId 用户id（可空，表示查询系统默认的等级配置，不查询关联用户等级）
     * @return 等级配置信息列表
     */
    ApiResult<List<CustomerLevelConfigDto>> getLevels(Long userId);

    /**
     * 获取用户等级晋升信息
     *
     * @param userId      用户id
     * @param targetLevel 晋升目标等级
     * @return 等级晋升信息
     */
    ApiResult<CustomerUpgradeLevelDto> getCustomerUpgradeInfo(Long userId, Integer targetLevel);
}
