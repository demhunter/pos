/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dao;

import com.pos.pos.domain.UserPosTransactionHandledInfo;
import com.pos.pos.dto.transaction.TransactionHandledInfoDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 交易手动处理Dao
 *
 * @author wangbing
 * @version 1.0, 2017/10/21
 */
@Repository
public interface PosUserTransactionHandledDao {

    /**
     * 保存处理信息
     *
     * @param handledInfo 处理信息
     */
    void save(@Param("handledInfo") UserPosTransactionHandledInfo handledInfo);

    /**
     * 手动处理信息
     *
     * @param recordId 交易记录id
     * @return 手动处理信息
     */
    TransactionHandledInfoDto getByRecordId(@Param("recordId") Long recordId);
}
