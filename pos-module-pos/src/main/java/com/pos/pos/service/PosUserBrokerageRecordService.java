/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.pos.dto.BrokerageHandledRecordDto;

import java.util.List;

/**
 * 快捷受收款用户佣金Service
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
public interface PosUserBrokerageRecordService {

    /**
     * 保存快捷收款佣金处理记录
     *
     * @param record 快捷收款佣金处理记录
     * @param user   操作人
     * @return 操作结果
     */
    ApiResult<NullObject> saveBrokerageRecord(BrokerageHandledRecordDto record, UserIdentifier user);
}
