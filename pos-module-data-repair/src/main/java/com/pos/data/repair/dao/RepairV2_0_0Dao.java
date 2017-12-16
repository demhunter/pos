/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.data.repair.dao;

import com.pos.authority.domain.CustomerPermission;
import com.pos.transaction.domain.TransactionCustomerBrokerage;
import com.pos.transaction.domain.UserPosTransactionHandledInfo;
import com.pos.transaction.domain.UserPosTransactionRecord;
import com.pos.transaction.domain.UserPosTwitterBrokerage;
import com.pos.transaction.dto.PosUserGetBrokerageRecordDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据修复Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/15
 */
@Repository
public interface RepairV2_0_0Dao {

    /**
     * 获取所有提现记录（PS：仅做数据修复查询）
     *
     * @return 提现记录
     */
    List<PosUserGetBrokerageRecordDto> queryAllRecords();


    /**
     * 保存佣金提现交易
     *
     * @param transaction 交易信息
     */
    void saveBrokerageTransaction(@Param("transaction") UserPosTransactionRecord transaction);

    /**
     * 获取指定用户的权限信息
     *
     * @param userId 用户id
     * @return 权限信息
     */
    CustomerPermission getPermission(@Param("userId") Long userId);

    /**
     * 获取指定用户组的权限信息
     *
     * @param userIds 用户id列表
     * @return 权限信息
     */
    List<CustomerPermission> getPermissions(@Param("userIds") List<Long> userIds);

    /**
     * 保存处理信息
     *
     * @param handledInfo 处理信息
     */
    void saveTransactionHandled(@Param("handledInfo") UserPosTransactionHandledInfo handledInfo);

    /**
     * 查询所有的推客分佣记录
     *
     * @return 分佣记录
     */
    List<UserPosTwitterBrokerage> queryAllTwitterBrokerage();

    /**
     * 批量保存生成的交易佣金信息
     *
     * @param brokerages 佣金列表
     */
    void saveCustomerBrokerages(@Param("brokerages") List<TransactionCustomerBrokerage> brokerages);

}
