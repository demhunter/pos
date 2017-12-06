/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dao;

import com.pos.transaction.domain.TransactionCustomerBrokerage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 客户佣金Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
@Repository
public interface CustomerBrokerageDao {

    /**
     * 批量保存生成的交易佣金信息
     *
     * @param brokerages 佣金列表
     */
    void saveBrokerages(List<TransactionCustomerBrokerage> brokerages);

    /**
     * 获取指定时间范围内的用户总佣金
     *
     * @param userId 用户id
     * @param begin  开始时间
     * @param end    结束时间
     * @return 总佣金
     */
    BigDecimal getDateSectionBrokerage(
            @Param("userId") Long userId,
            @Param("begin") Date begin,
            @Param("end") Date end);
}
