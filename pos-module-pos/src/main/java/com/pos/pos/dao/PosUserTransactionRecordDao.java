/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dao;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.pos.condition.query.PosTransactionCondition;
import com.pos.pos.domain.UserPosTransactionRecord;
import com.pos.pos.dto.transaction.TransactionSimpleStatisticsDto;
import com.pos.pos.dto.brokerage.BrokerageDailyStatisticsDto;
import com.pos.pos.dto.spread.SpreadGeneralInfoDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 交易记录Dao
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
@Repository
public interface PosUserTransactionRecordDao {

    /**
     * 标记已申请提现的记录为已提取
     *
     * @param userId 用户userId
     */
    void markTransactionRecordAgentStatus(
            @Param("userId") Long userId,
            @Param("fromAgentStatus") Byte fromAgentStatus,
            @Param("toAgentStatus") Byte toAgentStatus,
            @Param("deadline") Date deadline);

    /**
     * 查询用户推广概要信息
     *
     * @param user 用户标识
     * @return 推广概要信息
     */
    SpreadGeneralInfoDto querySpreadGeneralInfo(@Param("user") UserIdentifier user);

    /**
     * 查询每日记录数据统计
     *
     * @param user        用户标识
     * @param limitHelper 分页参数
     * @return 查询结果
     */
    List<BrokerageDailyStatisticsDto> queryDailyStatistics(
            @Param("user") UserIdentifier user,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询到截止时间用户可提现的金额
     *
     * @param user     用户标识
     * @param deadline 截止时间
     * @return 可提现金额
     */
    BigDecimal queryCurrentCanWithdrawDepositAmount(
            @Param("user") UserIdentifier user,
            @Param("deadline") Date deadline);

    /**
     * 查询符合条件的交易数量
     *
     * @param condition 查询条件
     * @return 交易数量
     */
    Integer queryTransactionRecordCount(@Param("condition") PosTransactionCondition condition);

    /**
     * 查询符合条件的交易列表
     *
     * @param condition   查询条件
     * @param orderHelper 分页参数
     * @param limitHelper 排序参数
     * @return 交易列表
     */
    List<UserPosTransactionRecord> queryTransactionRecord(
            @Param("condition") PosTransactionCondition condition,
            @Param("orderHelper") OrderHelper orderHelper,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询用户的交易统计信息
     *
     * @param userIds 用户列表
     * @return 统计信息
     */
    List<TransactionSimpleStatisticsDto> querySimpleStatistics(@Param("userIds") List<Long> userIds);

    /**
     * 更新用户付款银行卡信息<br>
     * PS：仅在数据修复时调用
     *
     * @param record 交易信息
     */
    void updateTransactionOutCardInfo(@Param("record") UserPosTransactionRecord record);
}
