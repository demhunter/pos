/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service_v;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.pos.dto.BrokerageHandledRecordDto;
import com.pos.pos.dto.twitter.TwitterDailyStatisticsDto;
import com.pos.pos.dto.twitter.TwitterGeneralInfoDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 推客相关Service
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
public interface TwitterService {

    /**
     * 初始化存在上级的推客
     *
     * @param userId       用户id
     * @param leaderUserId 上级推客userId
     */
    void initializeTwitter(Long userId, Long leaderUserId);

    /**
     * 初始化推客客户
     *
     * @param userId       用户id
     * @param leaderUserId 上级推客userId
     */
    void initializeTwitterCustomer(Long userId, Long leaderUserId);

    /**
     * 查询推客用户的概要信息
     *
     * @param twitterUserId 推客用户userId
     * @return 概要信息
     */
    ApiResult<TwitterGeneralInfoDto> queryTwitterGeneralInfo(Long twitterUserId);

    /**
     * 获取推客用户的每日收益
     *
     * @param twitterUserId 推客用户userId
     * @param limitHelper   分页参数
     * @return 每日收益
     */
    List<TwitterDailyStatisticsDto> queryTwitterDailyStatistics(Long twitterUserId, LimitHelper limitHelper);

    /**
     * 申请提现佣金
     *
     * @param user 申请提现
     * @return 申请提现金额
     */
    ApiResult<BigDecimal> applyWithdrawBrokerage(UserIdentifier user);

    /**
     * 查询用户提现记录
     *
     * @param user        用户标识
     * @param limitHelper 分页参数
     * @return 用户提现记录
     */
    List<BrokerageHandledRecordDto> queryBrokerageHandledRecord(UserIdentifier user, LimitHelper limitHelper);
}
