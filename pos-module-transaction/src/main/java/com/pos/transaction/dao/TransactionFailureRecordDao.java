/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dao;

import com.pos.transaction.domain.TransactionFailureRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
}
