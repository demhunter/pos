/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.pos.domain.UserPosChannelInfo;
import com.pos.pos.dto.develop.PosUserChildChannelDto;
import com.pos.pos.dto.twitter.TwitterGeneralInfoDto;
import com.pos.pos.dto.develop.DevelopGeneralInfoDto;
import com.pos.pos.dto.spread.SpreadCustomerDto;
import com.pos.pos.dto.spread.SpreadGeneralInfoDto;
import com.pos.pos.dto.twitter.ReferrerSimpleDto;
import com.pos.pos.dto.twitter.TwitterDailyStatisticsDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 快捷收款推客Service
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
public interface PosUserChannelInfoService {

    /**
     * 根据渠道商用户id获取渠道商信息
     *
     * @param channelUserId 渠道商用户id
     * @return 渠道商信息
     */
    UserPosChannelInfo get(Long channelUserId);

    /**
     * 查询推客用户的概要信息
     *
     * @param channelUserId 推客用户userId
     * @return 概要信息
     */
    ApiResult<TwitterGeneralInfoDto> queryTwitterGeneralInfo(Long channelUserId);

    /**
     * 获取推客用户的每日收益
     *
     * @param channelUserId 推客用户userId
     * @param limitHelper   分页参数
     * @return 每日收益
     */
    List<TwitterDailyStatisticsDto> queryTwitterDailyStatistics(Long channelUserId, LimitHelper limitHelper);

    /**
     * 申请提现佣金
     *
     * @param user 申请提现
     * @return 申请提现金额
     */
    ApiResult<BigDecimal> applyWithdrawBrokerage(UserIdentifier user);

    /**
     * 查询用户的推广快捷收款概要信息
     *
     * @param user 用户标识
     * @return 概要信息
     */
    ApiResult<SpreadGeneralInfoDto> getSpreadGeneralInfo(UserIdentifier user);

    /**
     * 查询推客用户推广的收款客户列表
     *
     * @param channelUserId 推客用户userId
     * @param limitHelper   分页参数
     * @return 推广的收款客户列表
     */
    ApiResult<Pagination<SpreadCustomerDto>> querySpreadCustomers(Long channelUserId, LimitHelper limitHelper);

    /**
     * 查询用户的发展下级推客概要信息
     *
     * @param channelUserId 推客用户userId
     * @return 发展下级推客概要信息
     */
    ApiResult<DevelopGeneralInfoDto> getDevelopGeneralInfo(Long channelUserId);

    /**
     * 查询用户发展的下级推客列表
     *
     * @param channelUserId 推客用户userId
     * @param limitHelper   分页参数
     * @return 发展的下级推客列表
     */
    ApiResult<Pagination<PosUserChildChannelDto>> queryDevelopTwitters(Long channelUserId, LimitHelper limitHelper);

    /**
     * 更新推客备忘录
     *
     * @param developId      推客主键id
     * @param remark         备忘录内容
     * @param operatorUserId 更新操作人userId
     * @return 更新结果
     */
    ApiResult<NullObject> updateTwitterRemark(Long developId, String remark, Long operatorUserId);

    /**
     * 查询推荐人简要信息
     *
     * @param referrerUserId 推荐人userId
     * @return 推荐人简要信息
     */
    ApiResult<ReferrerSimpleDto> findReferrerSimpleInfo(Long referrerUserId);

    /**
     * 查询用户发展的渠道商数量
     *
     * @param userId 用户id
     * @return 渠道商数量
     */
    Integer getUserDevelopCount(Long userId);
}
