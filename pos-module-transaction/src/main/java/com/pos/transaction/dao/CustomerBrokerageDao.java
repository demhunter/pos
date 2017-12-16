/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.transaction.domain.TransactionCustomerBrokerage;
import com.pos.transaction.dto.brokerage.BrokerageDailyStatisticsDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 客户佣金Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
@Repository
public interface CustomerBrokerageDao {

    /**
     * 批量保存生成的交易佣金信息
     *
     * @param brokerages 佣金列表
     */
    void saveBrokerages(@Param("brokerage") List<TransactionCustomerBrokerage> brokerages);

    /**
     * 获取指定时间范围内的用户总佣金
     *
     * @param userId 用户id
     * @param begin  开始时间
     * @param end    结束时间
     * @return 总佣金
     */
    BigDecimal getDateSectionBrokerage(
            @Param("userId") Long userId,
            @Param("begin") Date begin,
            @Param("end") Date end);

    /**
     * 查询用户每日收益
     *
     * @param userId      用户id
     * @param limitHelper 分页参数
     * @return 每日收益
     */
    List<BrokerageDailyStatisticsDto> queryDailyBrokerage(
            @Param("userId") Long userId,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询用户到截止日期的可提现佣金
     *
     * @param userId   用户id
     * @param deadline 截止日期
     * @return 可提现佣金
     */
    BigDecimal queryCanWithdrawBrokerage(
            @Param("userId") Long userId,
            @Param("deadline") Date deadline);

    /**
     * 更新客户佣金提取状态
     *
     * @param userId     用户id
     * @param fromStatus 初始状态
     * @param toStatus   目标状态
     * @param deadline   截止日期
     */
    void markBrokerageStatus(
            @Param("userId") Long userId,
            @Param("fromStatus") Integer fromStatus,
            @Param("toStatus") Integer toStatus,
            @Param("deadline") Date deadline);
}
