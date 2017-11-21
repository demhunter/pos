/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dao;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.pos.dto.PosUserGetBrokerageRecordDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 快捷收款用户佣金Dao
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
@Repository
public interface PosUserBrokerageRecordDao {

    /**
     * 新增快捷收款佣金处理记录
     *
     * @param record 快捷收款佣金处理记录
     */
    void save(@Param("record") PosUserGetBrokerageRecordDto record);

    /**
     * 查询用户提现记录
     *
     * @param user        用户标识
     * @param limitHelper 分页参数
     * @return 提现记录
     */
    List<PosUserGetBrokerageRecordDto> queryRecords(
            @Param("user") UserIdentifier user,
            @Param("limitHelper") LimitHelper limitHelper);
}
