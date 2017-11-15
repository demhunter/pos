/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.pos.condition.query.PosTransactionCondition;
import com.pos.pos.domain.PosTransaction;
import com.pos.pos.dto.transaction.TransactionSimpleStatisticsDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
    List<PosTransaction> queryTransactionRecord(
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
    void updateTransactionOutCardInfo(@Param("record") PosTransaction record);
}
