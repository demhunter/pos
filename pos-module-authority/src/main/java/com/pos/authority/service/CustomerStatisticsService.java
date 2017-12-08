/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service;

import com.pos.authority.dto.statistics.CustomerStatisticsDto;
import com.pos.authority.dto.statistics.DescendantStatisticsDto;

import java.math.BigDecimal;

/**
 * 客户统计Service
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
public interface CustomerStatisticsService {

    /**
     * 获取指定用户的统计 信息
     *
     * @param userId 用户id
     * @return 客户统计信息
     */
    CustomerStatisticsDto getStatistics(Long userId);

    /**
     * 获取用户的下级统计信息
     *
     * @param userId 用户id
     * @return 下级统计信息
     */
    DescendantStatisticsDto getDescendantStatistics(Long userId);

    /**
     * 累计用户直接下级数量（+1）
     *
     * @param parentUserId 父级用户id
     */
    void incrementChildrenCount(Long parentUserId);

    /**
     * 累计升级支付服务费
     *
     * @param userId        用户id
     * @param serviceCharge 支付金额
     */
    void incrementUpgradeCharge(Long userId, BigDecimal serviceCharge);

    /**
     * 累计收款金额
     *
     * @param userId         用户id
     * @param withdrawAmount 收款金额
     */
    void incrementWithdrawAmount(Long userId, BigDecimal withdrawAmount);

    /**
     * 累计佣金已提现金额
     *
     * @param userId    用户id
     * @param brokerage 佣金提现金额
     */
    void incrementWithdrawalBrokerage(Long userId, BigDecimal brokerage);
}
