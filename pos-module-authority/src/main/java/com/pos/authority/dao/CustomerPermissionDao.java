/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dao;

import com.pos.authority.domain.CustomerPermission;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 客户权限DAO
 *
 * @author wangbing
 * @version 1.0, 2017/11/24
 */
@Repository
public interface CustomerPermissionDao {

    /**
     * 保存客户权限信息
     *
     * @param permission 权限信息
     */
    void save(@Param("permission") CustomerPermission permission);

    /**
     * 获取指定用户的权限信息
     *
     * @param userId 用户id
     * @return 权限信息
     */
    CustomerPermission getPermission(@Param("userId") Long userId);

    /**
     * 更新用户身份认证的实名部分信息
     *
     * @param permission 身份认证的实名部分信息
     */
    void updateCustomerIdentity(@Param("permission") CustomerPermissionDto permission);

    /**
     * 更新用户的收款银行卡
     *
     * @param permission 收款银行卡相关信息
     */
    void updateWithdrawCard(@Param("permission") CustomerPermissionDto permission);

    /**
     * 更新用户身份认证人审核状态
     *
     * @param userId       用户id
     * @param status       目标审核状态值
     * @param rejectReason 审核未通过原因
     * @param updateUserId 更新操作人id
     */
    void updateAuditStatus(
            @Param("userId") Long userId,
            @Param("status") Integer status,
            @Param("rejectReason") String rejectReason,
            @Param("updateUserId") Long updateUserId);

    /**
     * 更新用户等级收款配置信息
     *
     * @param permission 等级收款配置信息
     */
    void updateLevelConfig(@Param("permission") CustomerPermissionDto permission);
}
