/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dao;

import com.pos.pos.domain.PosTransaction;
import com.pos.pos.dto.BankLogoDto;
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

    List<PosTransaction> queryRecordByUserIdAndCostType(
            @Param("userId") long userId,
            @Param("costType") int costType);

    PosTransaction queryRecordById(@Param("id") long id);

    PosTransaction queryRecordByRecordNum(@Param("recordNum") String recordNum);

    void addUserPosRecord(@Param("record") PosTransaction posTransaction);

    void updatePosRecord(@Param("record") PosTransaction posTransaction);

    List<BankLogoDto> queryBankLogo();

    void updateRemark(@Param("remark") String remark, @Param("channelId") long channelId);
}
