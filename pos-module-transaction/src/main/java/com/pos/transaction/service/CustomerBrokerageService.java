/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.transaction.dto.brokerage.BrokerageDailyStatisticsDto;
import com.pos.transaction.dto.brokerage.BrokerageGeneralInfoDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户佣金Service
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
public interface CustomerBrokerageService {

    /**
     * 获取用户的佣金概要统计信息
     *
     * @param userId 用户id
     * @return 佣金概要统计信息
     */
    BrokerageGeneralInfoDto getBrokerageGeneral(Long userId);

    /**
     * 获取用户每日收益
     *
     * @param userId       用户id
     * @param limitHelper  分页参数
     * @param queriedMonth 是否插入月份分栏统计
     * @return 每日收益
     */
    List<BrokerageDailyStatisticsDto> queryDailyBrokerage(Long userId, LimitHelper limitHelper, boolean queriedMonth);

    /**
     * 佣金提现
     *
     * @param userId 用户id
     * @return 佣金提现金额
     */
    ApiResult<BigDecimal> withdrawBrokerage(Long userId);
}
