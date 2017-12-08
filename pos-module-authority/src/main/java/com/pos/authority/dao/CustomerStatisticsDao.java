/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dao;

import com.pos.authority.domain.CustomerStatistics;
import com.pos.authority.dto.statistics.CustomerStatisticsDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 客户统计信息Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/4
 */
@Repository
public interface CustomerStatisticsDao {

    /**
     * 保存客户统计信息
     *
     * @param statistics 客户统计信息
     */
    void save(@Param("statistics") CustomerStatistics statistics);

    /**
     * 根据用户id获取统计信息
     *
     * @param userId 用户id
     * @return 客户统计信息
     */
    CustomerStatisticsDto getByUserId(@Param("userId") Long userId);

    /**
     * 以原子形式增加用户的直接下级客户数量（+ 1）
     *
     * @param userId 用户id
     */
    void incrementChildrenCount(@Param("userId") Long userId);

    /**
     * 已原子形式累计用户支付的等级晋升服务费
     *
     * @param userId     用户id
     * @param paidCharge 晋升服务费
     */
    void incrementPaidCharge(
            @Param("userId") Long userId,
            @Param("paidCharge") BigDecimal paidCharge);

    /**
     * 已原子形式累计用户收款金额
     *
     * @param userId         用户id
     * @param withdrawAmount 收款金额
     */
    void incrementWithdrawAmount(
            @Param("userId") Long userId,
            @Param("withdrawAmount") BigDecimal withdrawAmount);

    /**
     * 以原子形式累计用户佣金提现金额和提现次数
     *
     * @param userId              用户id
     * @param withdrawalBrokerage 佣金提现金额
     */
    void incrementWithdrawalBrokerage(
            @Param("userId") Long userId,
            @Param("withdrawalBrokerage") BigDecimal withdrawalBrokerage);
}
