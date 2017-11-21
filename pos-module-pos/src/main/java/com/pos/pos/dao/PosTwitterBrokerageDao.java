/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.pos.domain.UserPosTwitterBrokerage;
import com.pos.pos.dto.twitter.TwitterBrokerageStatisticsDto;
import com.pos.pos.dto.twitter.TwitterDailyStatisticsDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 推客佣金Dao
 *
 * @author wangbing
 * @version 1.0, 2017/10/20
 */
@Repository
public interface PosTwitterBrokerageDao {

    /**
     * 保存交易佣金信息
     *
     * @param brokerage 交易佣金信息
     */
    void save(@Param("brokerage") UserPosTwitterBrokerage brokerage);

    /**
     * 查询推客的每日收益
     *
     * @param channelUserId 推客用户UserId
     * @param limitHelper   分页参数
     * @return 每日收益
     */
    List<TwitterDailyStatisticsDto> queryTwitterDailyStatistics(
            @Param("channelUserId") Long channelUserId,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询推客到截止时间的可提余额
     *
     * @param channelUserId 推客用户UserId
     * @param deadline      查询截止时间
     * @return 可提余额
     */
    BigDecimal queryTwitterCanApplyMoney(
            @Param("channelUserId") Long channelUserId,
            @Param("deadline") Date deadline);

    /**
     * 获取指定时间区间内的收益
     *
     * @param channelUserId 推客用户UserId
     * @param beginTime     开始时间
     * @param endTime       截止时间
     * @return 收益
     */
    BigDecimal queryTwitterDateSectionBrokerage(
            @Param("channelUserId") Long channelUserId,
            @Param("beginTime") Date beginTime,
            @Param("endTime") Date endTime);

    /**
     * 更新推客客户佣金提取状态
     *
     * @param userId 用户userId
     */
    void markAgentStatus(
            @Param("userId") Long userId,
            @Param("fromAgentStatus") Byte fromAgentStatus,
            @Param("toAgentStatus") Byte toAgentStatus,
            @Param("deadline") Date deadline);

    /**
     * 更新推客推客佣金提取状态
     *
     * @param userId 用户userId
     */
    void markParentAgentStatus(
            @Param("userId") Long userId,
            @Param("fromAgentStatus") Byte fromAgentStatus,
            @Param("toAgentStatus") Byte toAgentStatus,
            @Param("deadline") Date deadline);

    /**
     * 查询指定客户列表为推客带来的佣金
     *
     * @param userIds 客户列表
     * @return key = 客户userId
     */
    List<TwitterBrokerageStatisticsDto> queryAgentBrokerageMap(@Param("userIds") List<Long> userIds);

    /**
     * 查询指定推客列表为父推客带来的佣金
     *
     * @param userIds 推客列表
     * @return key = 推客userId
     */
    List<TwitterBrokerageStatisticsDto> queryParentAgentBrokerageMap(@Param("userIds") List<Long> userIds);
}
