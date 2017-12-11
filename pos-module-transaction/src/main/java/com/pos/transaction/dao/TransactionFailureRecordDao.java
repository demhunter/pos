/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dao;

import com.pos.transaction.domain.TransactionFailureRecord;
import com.pos.transaction.dto.failure.TransactionFailureRecordDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 交易失败记录Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/6
 */
@Repository
public interface TransactionFailureRecordDao {

    /**
     * 保存交易失败信息
     *
     * @param failureRecord 交易失败信息
     */
    void save(@Param("failureRecord") TransactionFailureRecord failureRecord);

    /**
     * 查询交易的失败记录
     *
     * @param transactionId 交易id
     * @return 交易失败信息
     */
    List<TransactionFailureRecordDto> queryFailureRecords(@Param("transactionId") Long transactionId);
}
