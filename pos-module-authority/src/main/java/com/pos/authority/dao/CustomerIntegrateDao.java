/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dao;

import com.pos.authority.condition.query.CustomerIntegrateCondition;
import com.pos.authority.dto.customer.CustomerIntegrateInfoDto;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户聚合数据Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/11
 */
@Repository
public interface CustomerIntegrateDao {

    /**
     * 查询符合条件的用户数量
     *
     * @param condition 查询条件
     * @return 用户数量
     */
    int getCustomerIntegrateCount(@Param("condition") CustomerIntegrateCondition condition);

    /**
     * 查询符合条件的用户聚合数据列表
     *
     * @param condition   查询条件
     * @param orderHelper 排序参数
     * @param limitHelper 分页参数
     * @return 用户聚合数据列表
     */
    List<CustomerIntegrateInfoDto> queryCustomerIntegrates(
            @Param("condition") CustomerIntegrateCondition condition,
            @Param("orderHelper") OrderHelper orderHelper,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询指定用户的聚合数据
     *
     * @param userId 用户id
     * @return 聚合数据
     */
    CustomerIntegrateInfoDto findCustomerIntegrate(@Param("userId") Long userId);
}
