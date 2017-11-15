/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service;

import com.pos.common.util.mvc.support.*;
import com.pos.basic.dto.UserIdentifier;
import com.pos.pos.condition.query.PosUserCondition;
import com.pos.pos.dto.auth.BaseAuthDto;
import com.pos.pos.dto.user.PosUserIntegrateDto;

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
}
