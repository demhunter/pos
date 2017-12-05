/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dao;

import com.pos.transaction.domain.UserPosTransactionRecord;
import com.pos.transaction.dto.BankLogoDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
@Repository
public interface PosDao {

    List<Long> getCompanyIds();

    List<UserPosTransactionRecord> queryRecordByUserIdAndCostType(@Param("userId") long userId, @Param("costType")
            int costType);

    UserPosTransactionRecord queryRecordById(@Param("id") long id);

    UserPosTransactionRecord queryRecordByRecordNum(@Param("recordNum") String recordNum);

    void addUserPosRecord(@Param("record") UserPosTransactionRecord userPosTransactionRecord);

    void updatePosRecord(@Param("record") UserPosTransactionRecord userPosTransactionRecord);

    List<BankLogoDto> queryBankLogo();

    void updateRemark(@Param("remark") String remark, @Param("channelId") long channelId);
}
