/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dao;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.transaction.condition.query.PosTransactionCondition;
import com.pos.transaction.domain.UserPosTransactionRecord;
import com.pos.transaction.dto.transaction.TransactionSimpleStatisticsDto;
import com.pos.transaction.dto.brokerage.BrokerageDailyStatisticsDto;
import com.pos.transaction.dto.spread.SpreadGeneralInfoDto;
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
     * 获取指定的交易信息
     *
     * @param transactionId 交易id
     * @return 交易信息
     */
    UserPosTransactionRecord get(@Param("transactionId") Long transactionId);

    /**
     * 保存交易信息
     *
     * @param transaction 交易信息
     */
    void saveNormalTransaction(@Param("transaction") UserPosTransactionRecord transaction);

    /**
     * 保存佣金提现交易
     *
     * @param transaction 交易信息
     */
    void saveBrokerageTransaction(@Param("transaction") UserPosTransactionRecord transaction);

    /**
     * 更新交易信息
     *
     * @param transaction 交易信息
     */
    void updateTransaction(@Param("transaction") UserPosTransactionRecord transaction);

    /**
     * 已原子形式增加交易失败次数（+1）
     *
     * @param transactionId 交易id
     */
    void incrementFailureTimes(Long transactionId);

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
    @Deprecated
    void updateTransactionOutCardInfo(@Param("record") UserPosTransactionRecord record);
}
