/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.pos.domain.TwitterBrokerage;
import com.pos.pos.dto.twitter.TwitterDailyStatisticsDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 推客佣金Dao
 *
 * @author wangbing
 * @version 1.0, 2017/10/20
 */
@Repository
public interface TwitterBrokerageDao {

    /**
     * 保存交易佣金信息
     *
     * @param brokerage 交易佣金信息
     */
    void save(@Param("brokerage") TwitterBrokerage brokerage);

    /**
     * 查询推客的每日收益
     *
     * @param twitterUserId 推客用户UserId
     * @param limitHelper   分页参数
     * @return 每日收益
     */
    List<TwitterDailyStatisticsDto> queryTwitterDailyStatistics(
            @Param("twitterUserId") Long twitterUserId,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询推客到截止时间的可提余额
     *
     * @param twitterUserId 推客用户UserId
     * @param deadline      查询截止时间
     * @return 可提余额
     */
    BigDecimal queryTwitterCanApplyMoney(
            @Param("twitterUserId") Long twitterUserId,
            @Param("deadline") Date deadline);

    /**
     * 获取指定时间区间内的收益
     *
     * @param twitterUserId 推客用户UserId
     * @param beginTime     开始时间
     * @param endTime       截止时间
     * @return 收益
     */
    BigDecimal queryTwitterDateSectionBrokerage(
            @Param("twitterUserId") Long twitterUserId,
            @Param("beginTime") Date beginTime,
            @Param("endTime") Date endTime);

    /**
     * 更新推客佣金提取状态
     *
     * @param userId     推客用户id
     * @param fromStatus 初始状态
     * @param toStatus   转化状态
     * @param deadline   截止时间
     */
    void markTwitterStatus(
            @Param("userId") Long userId,
            @Param("fromStatus") Byte fromStatus,
            @Param("toStatus") Byte toStatus,
            @Param("deadline") Date deadline);

    /**
     * 更新父推客佣金提取状态
     *
     * @param userId     父推客用户id
     * @param fromStatus 初始状态
     * @param toStatus   转化状态
     * @param deadline   截止时间
     */
    void markParentStatus(
            @Param("userId") Long userId,
            @Param("fromStatus") Byte fromStatus,
            @Param("toStatus") Byte toStatus,
            @Param("deadline") Date deadline);

    /**
     * 查询指定客户列表为推客带来的佣金
     *
     * @param userIds 客户列表
     * @return key = 客户userId
     */
    Map<Long, BigDecimal> queryAgentBrokerageMap(@Param("userIds") List<Long> userIds);

    /**
     * 查询指定推客列表为父推客带来的佣金
     *
     * @param userIds 推客列表
     * @return key = 推客userId
     */
    Map<Long, BigDecimal> queryParentAgentBrokerageMap(@Param("userIds") List<Long> userIds);
}
