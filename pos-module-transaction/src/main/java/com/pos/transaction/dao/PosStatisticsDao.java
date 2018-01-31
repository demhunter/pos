/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dao;

import com.pos.basic.dto.CommonEnumDto;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.transaction.dto.statistics.PosGeneralStatisticsDto;
import com.pos.transaction.dto.statistics.TransactionDailyStatisticsDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 交易数据每日统计Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/13
 */
@Repository
public interface PosStatisticsDao {

    /**
     * 查询数量
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @return 数量
     */
    int queryDailyCount(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 查询每日数据统计
     *
     * @param beginTime   开始时间
     * @param endTime     截止时间
     * @param limitHelper 分页参数
     * @return 查询结果
     */
    List<TransactionDailyStatisticsDto> queryDailyStatistics(
            @Param("beginTime") Date beginTime,
            @Param("endTime") Date endTime,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询用户的等级分布
     *
     * @return 等级分布
     */
    List<CommonEnumDto> queryLevelDistribution();

    /**
     * 查询用户实名认证分布
     *
     * @return 实名认证分布
     */
    List<CommonEnumDto> queryAuditDistribution();

    /**
     * 查询收款统计
     *
     * @return 收款统计
     */
    PosGeneralStatisticsDto queryPosStatistics();

    /**
     * 查询所有收款统计-按日返回
     *
     * @return 收款统计
     */
    List<TransactionDailyStatisticsDto> queryPosStatisticsByDaily(
            @Param("begin") Date begin, @Param("end") Date end);

    /**
     * 查询佣金提现统计
     *
     * @return 佣金提现统计
     */
    PosGeneralStatisticsDto queryBrokerageWithdrawalStatistics();

    /**
     * 查询佣金提现统计-按日返回
     *
     * @return 佣金提现统计
     */
    List<TransactionDailyStatisticsDto> queryBrokerageWithdrawalStatisticsByDaily(
            @Param("begin") Date begin, @Param("end") Date end);

    /**
     * 查询佣金统计
     *
     * @return 佣金统计
     */
    PosGeneralStatisticsDto queryBrokerageStatistics();

    /**
     * 获取当前最新的一条数据统计
     *
     * @return 最新数据统计
     */
    TransactionDailyStatisticsDto getLatest();

    /**
     * 保存全部每日数据
     *
     * @param dailies 每日数据
     */
    void saveAll(@Param("dailies") List<TransactionDailyStatisticsDto> dailies);
}
