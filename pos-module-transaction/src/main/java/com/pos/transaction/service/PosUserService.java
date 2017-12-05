/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service;

import com.pos.common.util.mvc.support.*;
import com.pos.basic.dto.UserIdentifier;
import com.pos.transaction.condition.query.PosUserCondition;
import com.pos.transaction.dto.user.PosUserIntegrateDto;
import com.pos.transaction.dto.auth.BaseAuthDto;

/**
 * 快捷收款用户Service
 *
 * @author wangbing
 * @version 1.0, 2017/8/25
 */
public interface PosUserService {

    /**
     * 查询符合条件的快捷收款用户聚合信息
     *
     * @param condition   查询条件
     * @param orderHelper 排序参数
     * @param limitHelper 分页参数
     * @return 查询结果
     */
    Pagination<PosUserIntegrateDto> queryPosUsers(PosUserCondition condition, OrderHelper orderHelper, LimitHelper limitHelper);

    /**
     * 更改快捷收款用户权限信息
     *
     * @param posId    被更新用户posID
     * @param baseAuth 新权限信息
     * @param user     操作人
     * @return 操作结果
     */
    ApiResult<NullObject> updatePosUserAuth(Long posId, BaseAuthDto baseAuth, UserIdentifier user);

    /**
     * 获取指定用户的基本权限信息
     *
     * @param posId 主键id
     * @return 基本权限信息
     */
    ApiResult<BaseAuthDto> getBaseAuthById(Long posId);
}
