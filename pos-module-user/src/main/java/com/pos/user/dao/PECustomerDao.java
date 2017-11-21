/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.user.condition.query.PECustomerCondition;
import com.pos.user.domain.PECustomers;
import com.pos.user.dto.platformEmployee.PECustomerDetailDto;
import com.pos.user.dto.platformEmployee.PECustomerStatisticsDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 家居顾问客户关联Dao
 *
 * @author wangbing
 * @version 1.0, 2017/7/6
 */
@Repository
public interface PECustomerDao {

    /**
     * 查询指定家居顾问的关联客户
     *
     * @param peUserId 家居顾问id
     * @param status   关联客户状态
     * @return 家居顾问的关联客户列表
     */
    List<PECustomers> queryCustomerRelation(
            @Param("peUserId") Long peUserId,
            @Param("status") Byte status);

    /**
     * 查询指定家居顾问的关联客户详细信息
     *
     * @param peUserId    家居顾问id
     * @param orderHelper 排序参数
     * @param limitHelper 分页参数
     * @return 家居顾问的关联客户详细信息列表
     */
    List<PECustomerDetailDto> queryCustomerDetail(
            @Param("peUserId") Long peUserId,
            @Param("orderHelper") OrderHelper orderHelper,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询指定家居顾问的客户总数
     *
     * @param peUserId 家居顾问userId
     * @return 客户总数
     */
    int queryCustomerCount(@Param("peUserId") Long peUserId);


    /**
     * 查询各家居顾问列表的客户总数
     *
     * @param userIds 家居顾问userIds
     * @return 客户总数
     */
    List<PECustomerStatisticsDto> queryCustomerCountByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 根据家居顾问userId和客户userId获取关联客户信息
     *
     * @param peUserId 家居顾问userId
     * @param cUserId  客户userId
     * @return 关联客户信息
     */
    PECustomers getCustomer(
            @Param("peUserId") Long peUserId,
            @Param("cUserId") Long cUserId);

    /**
     * 根据客户userId获取关联信息
     *
     * @param cUserId 客户userId
     * @return 关联信息
     */
    PECustomers getByCUserId(@Param("cUserId") Long cUserId, @Param("areaId") Long areaId);

    /**
     * 更新家居顾问客户关联
     *
     * @param peCustomers 家居顾问客户关联信息
     */
    void updateCustomer(@Param("peCustomers") PECustomers peCustomers);

    /**
     * 保存家居顾问客户关联
     *
     * @param peCustomers 家居顾问客户关联信息
     */
    void save(@Param("peCustomers") PECustomers peCustomers);

    /**
     * 查询家居顾问对于的客户信息
     *
     * @param condition
     * @return
     */
    List<PECustomers> queryCustomerUserIds(@Param("condition") PECustomerCondition condition);
}
