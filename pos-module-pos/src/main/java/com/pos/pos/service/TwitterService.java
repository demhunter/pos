/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.pos.dto.BrokerageHandledRecordDto;
import com.pos.pos.dto.develop.DevelopGeneralInfoDto;
import com.pos.pos.dto.develop.ChildTwitterDto;
import com.pos.pos.dto.spread.SpreadCustomerDto;
import com.pos.pos.dto.spread.SpreadGeneralInfoDto;
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
     * 初始化推客信息
     *
     * @param userId        用户id
     * @param twitterUserId 上级推客userId(可空，表示初始化成种子推客)
     */
    void initializeTwitter(Long userId, Long twitterUserId);

    /**
     * 初始化推客客户关系
     *
     * @param userId        用户id
     * @param twitterUserId 上级推客userId
     */
    void initializeTwitterCustomer(Long userId, Long twitterUserId);

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
     * 保存快捷收款佣金处理记录
     *
     * @param record 快捷收款佣金处理记录
     * @param user   操作人
     * @return 操作结果
     */
    ApiResult<NullObject> saveBrokerageRecord(BrokerageHandledRecordDto record, UserIdentifier user);

    /**
     * 查询用户提现记录
     *
     * @param user        用户标识
     * @param limitHelper 分页参数
     * @return 用户提现记录
     */
    List<BrokerageHandledRecordDto> queryBrokerageHandledRecord(UserIdentifier user, LimitHelper limitHelper);

    /**
     * 查询用户的发展下级推客概要信息
     *
     * @param twitterUserId 推客用户userId
     * @return 发展下级推客概要信息
     */
    ApiResult<DevelopGeneralInfoDto> getDevelopGeneralInfo(Long twitterUserId);

    /**
     * 查询用户发展的下级推客列表
     *
     * @param twitterUserId 推客用户userId
     * @param limitHelper   分页参数
     * @return 发展的下级推客列表
     */
    ApiResult<Pagination<ChildTwitterDto>> queryDevelopTwitters(Long twitterUserId, LimitHelper limitHelper);

    /**
     * 更新推客备忘录
     *
     * @param developId      推客主键id
     * @param remark         备忘录内容
     * @param operatorUserId 更新操作人userId
     * @return 更新结果
     */
    ApiResult<NullObject> updateChildTwitterRemark(Long developId, String remark, Long operatorUserId);

    /**
     * 查询用户的推广快捷收款概要信息
     *
     * @param user 用户标识
     * @return 概要信息
     */
    ApiResult<SpreadGeneralInfoDto> getSpreadGeneralInfo(UserIdentifier user);

    /**
     * 查询推客用户推广的收款客户列表 TODO 待完善
     *
     * @param twitterUserId 推客用户userId
     * @param limitHelper   分页参数
     * @return 推广的收款客户列表
     */
    ApiResult<Pagination<SpreadCustomerDto>> querySpreadCustomers(Long twitterUserId, LimitHelper limitHelper);
}
