/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service;

import com.pos.authority.condition.query.CustomerIntegrateCondition;
import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.domain.CustomerLevelConfig;
import com.pos.authority.dto.CustomerEnumsDto;
import com.pos.authority.dto.customer.CustomerIntegrateInfoDto;
import com.pos.authority.dto.identity.CustomerIdentityDto;
import com.pos.authority.dto.level.CustomerLevelConfigDto;
import com.pos.authority.dto.level.CustomerUpgradeLevelDto;
import com.pos.authority.dto.permission.CustomerPermissionBasicDto;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.authority.fsm.context.AuditStatusTransferContext;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.*;

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
     * 获取指定用户的权限基础信息
     *
     * @param userId 用户id
     * @return 权限基础信息
     */
    CustomerPermissionBasicDto getPermissionBasicInfo(Long userId);

    /**
     * 更新用户收款权限相关信息
     *
     * @param basicPermission 新信息
     * @param operator        更新操作人标识
     * @return 操作结果
     */
    ApiResult<NullObject> updatePermission(CustomerPermissionBasicDto basicPermission, UserIdentifier operator);

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

    /**
     * 获取用户已提交认证的实名信息
     *
     * @param userId 用户id
     * @return 身份实名信息
     */
    CustomerIdentityDto getCustomerIdentity(Long userId);

    /**
     * 解密身份认证数据
     *
     * @param identity 身份认证信息
     */
    void decryptedCustomerIdentity(CustomerIdentityDto identity);

    /**
     * 更新用户身份认证的实名信息
     *
     * @param userId   用户id
     * @param identity 实名信息
     * @return 更新结果
     */
    ApiResult<NullObject> updateCustomerIdentity(Long userId, CustomerIdentityDto identity);

    /**
     * 更新用户绑定过的收款银行卡
     *
     * @param permission 收款银行卡信息
     * @return 更新结果
     */
    ApiResult<NullObject> updateWithdrawCard(CustomerPermissionDto permission);

    /**
     * 更新用户身份认证审核状态
     *
     * @param transferContext 状态转换上下文，包含操作信息
     * @param auditStatus     新身份认证审核状态
     */
    boolean updateAuditStatus(AuditStatusTransferContext transferContext, CustomerAuditStatus auditStatus);

    /**
     * 用户等级晋升
     *
     * @param permission        用户当前权限等级信息
     * @param targetLevelConfig 晋升目标等级配置
     * @param operatorUserId    等级晋升操作人id
     */
    void upgradeLevel(CustomerPermissionDto permission, CustomerLevelConfig targetLevelConfig, Long operatorUserId);

    /**
     * 查询与用户相关的枚举
     *
     * @return 用户相关的枚举
     */
    ApiResult<CustomerEnumsDto> queryPosCustomerEnums();

    /**
     * 按条件查询用户聚合数据列表
     *
     * @param condition   查询条件
     * @param orderHelper 排序参数
     * @param limitHelper 分页参数
     * @return 聚合数据
     */
    Pagination<CustomerIntegrateInfoDto> queryCustomerIntegrates(CustomerIntegrateCondition condition, OrderHelper orderHelper, LimitHelper limitHelper);

    /**
     * 查询指定用户的聚合数据
     *
     * @param userId 用户id
     * @return 聚合数据
     */
    CustomerIntegrateInfoDto findCustomerIntegrate(Long userId);

    /**
     * 启用或禁用用户
     *
     * @param userId    用户od
     * @param available true：启用；false：禁用
     * @param operator  操作人用户标识
     * @return 操作结果
     */
    ApiResult<NullObject> updateUserAvailable(Long userId, Boolean available, UserIdentifier operator);
}
