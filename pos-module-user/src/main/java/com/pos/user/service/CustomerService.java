/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.user.condition.query.CustomerListCondition;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.user.condition.query.CustomerConsoleListCondition;
import com.pos.user.condition.query.UserListCondition;
import com.pos.user.constant.CustomerType;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.customer.CustomerRemarksDto;

import java.util.List;
import java.util.Map;

/**
 * 客户相关Service
 *
 * @author cc
 * @version 1.0, 16/8/4
 */
public interface CustomerService {

    /**
     * 查询客户数
     *
     * @param available 标识是否有效，如为空则表示不加筛选条件
     * @return 查询结果
     */
    int getCustomerCount(Boolean available, CustomerConsoleListCondition condition);

    /**
     * 根据用户ID查询客户信息.
     *
     * @param userId  用户ID
     * @param disable 是否返回被禁用的账号
     * @param deleted 是否返回被删除的账号
     */
    CustomerDto findById(Long userId, boolean disable, boolean deleted);

    /**
     * 根据用户名查询客户信息.
     *
     * @param userName 用户名
     * @param disable  是否返回被禁用的账号
     * @param deleted  是否返回被删除的账号
     */
    CustomerDto findByUserName(String userName, boolean disable, boolean deleted);

    /**
     * 根据用户手机ID查询客户信息.
     *
     * @param userPhone 手机号
     * @param disable   是否返回被禁用的账号
     * @param deleted   是否返回被删除的账号
     * @return 如果用户不存在, 返回调用失败的错误码
     */
    CustomerDto findByUserPhone(String userPhone, boolean disable, boolean deleted);

    /**
     * 查询指定的一组客户信息.
     *
     * @param userIds 一组用户ID
     * @param disable 是否返回被禁用的账号
     * @param deleted 是否返回被删除的账号
     */
    List<CustomerDto> findInUserIds(List<Long> userIds, boolean disable, boolean deleted);

    /**
     * 更新客户信息, 仅当数据有修改时才执行update.
     * <p>
     * 注意: 未设置的属性值在更新后将被覆盖！建议先调用findById等相关方法以填充数据并拷贝修改前的对象.
     * </p>
     * <i>example:</i>
     * <pre>
     *     ApiResult<CustomerDto> result = service.findById(id, disable, deleted); // 查询以填充数据
     *     CustomerDto beforeDto = result.getData().copy(); // 修改前先拷贝对象
     *     result.getData().setXXX(...); // 开始修改数据
     *     ...
     *     service.update(beforeDto, result.getData()); // 修改完毕调用更新方法
     * </pre>
     *
     * @param beforeDto 修改前的用户数据
     * @param afterDto  修改后的用户数据
     * @return 数据有被更新返回true, 否则返回false
     */
    boolean update(CustomerDto beforeDto, CustomerDto afterDto);

    /**
     * 查询客户列表
     *
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return 查询结果
     */
    List<CustomerDto> getCustomers(CustomerConsoleListCondition condition, LimitHelper limitHelper, OrderHelper orderHelper);

    /**
     * 查询与商家产生过会话的客户列表
     *
     * @param condition   查询条件
     * @param limitHelper 分页参数
     * @return 查询结果
     */
    List<CustomerDto> getCustomersByCompany(CustomerListCondition condition, LimitHelper limitHelper);

    /**
     * 查询与商家产生过会话的客户数
     *
     * @param condition 查询条件
     * @return 查询结果
     */
    int getCustomerCountByCompany(CustomerListCondition condition);

    /**
     * 查询客户IDs
     *
     * @param userListCondition
     * @return
     */
    List<Long> findUserIdInfo(UserListCondition userListCondition);

    /**
     * 查询客户信息
     *
     * @param userIds
     * @return
     */
    Map<Long, CustomerDto> getCustomerDtoMapById(List<Long> userIds);

    void updateCustomerInfo(Long userId, CustomerType customerType);

    ApiResult<NullObject> updateCustomerSimple(CustomerRemarksDto customerInfo);
}
