/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.user.condition.query.CustomerConsoleListCondition;
import com.pos.user.condition.query.CustomerListCondition;
import com.pos.user.condition.query.UserListCondition;
import com.pos.user.domain.Customer;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * C端用户相关DAO.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
@Repository
public interface CustomerDao {

    void save(@Param("c") Customer customer);

    void update(@Param("c") Customer customer);

    Customer getByUserId(@Param("userId") Long userId);

    CustomerDto findCustomerByUserId(
            @Param("userId") Long userId,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    CustomerDto findCustomerByUserName(
            @Param("userName") String userName,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    CustomerDto findCustomerByUserPhone(
            @Param("userPhone") String userPhone,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    List<CustomerDto> findCustomersInUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    /**
     * 查询客户列表
     *
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return 查询结果
     */
    List<CustomerDto> getCustomers(
            @Param("condition") CustomerConsoleListCondition condition,
            @Param("limitHelper") LimitHelper limitHelper,
            @Param("orderHelper") OrderHelper orderHelper);

    /**
     * 查询客户数
     *
     * @param available 是否可用
     * @return 查询结果
     */
    int getCustomerCount(@Param("available") Boolean available,@Param("condition") CustomerConsoleListCondition condition);

    /**
     * 查询与商家产生过会话的客户列表
     *
     * @param condition 查询条件
     * @param limitHelper 分页参数
     * @return 查询结果
     */
    List<CustomerDto> getCustomersByCompany(
            @Param("condition") CustomerListCondition condition,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询与商家产生过会话的客户数
     *
     * @param condition 查询条件
     * @return 查询结果
     */
    int getCustomerCountByCompany(@Param("condition") CustomerListCondition condition);

    /**
     * 查询客户IDs
     *
     * @param userListCondition
     * @return
     */
    List<Long> findUserIdInfo(@Param("userListCondition") UserListCondition userListCondition);

    Customer findCustomerById(@Param("userId") Long userId);
    /**
     * 查询用户列表
     *
     * @param deleted     是否返回被删除的账号
     * @param available   是否返回被禁用的账号
     * @param limitHelper 分页参数
     * @return 用户列表
     */
    List<CustomerDto> queryCustomers(
            @Param("deleted") Boolean deleted,
            @Param("available") Boolean available,
            @Param("limitHelper") LimitHelper limitHelper);


}
