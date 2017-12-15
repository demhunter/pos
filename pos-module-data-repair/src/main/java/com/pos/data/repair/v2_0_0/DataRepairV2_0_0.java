/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.data.repair.v2_0_0;

import com.google.common.collect.Lists;
import com.pos.authority.domain.CustomerPermission;
import com.pos.common.util.basic.UUIDUnsigned32;
import com.pos.common.util.exception.ErrorCode;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.data.repair.dao.RepairV2_0_0Dao;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.constants.TransactionStatusType;
import com.pos.transaction.constants.TransactionType;
import com.pos.transaction.domain.UserPosTransactionHandledInfo;
import com.pos.transaction.domain.UserPosTransactionRecord;
import com.pos.transaction.dto.PosUserGetBrokerageRecordDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * V2.0.0 数据修复支持类
 *
 * @author wangbing
 * @version 1.0, 2017/12/15
 */
@Component
public class DataRepairV2_0_0 {

    private static final Logger LOG = LoggerFactory.getLogger(DataRepairV2_0_0.class);


    @Resource
    private RepairV2_0_0Dao repairV2_0_0Dao;

    @Resource
    private PosConstants posConstants;

    public ApiResult<List<Long>> repairBrokerageTransaction() {
        LOG.info("开始修复佣金提现交易......");
        List<PosUserGetBrokerageRecordDto> getRecords = repairV2_0_0Dao.queryAllRecords();
        if (CollectionUtils.isEmpty(getRecords)) {
            LOG.info("没有待修复的佣金提现交易");
            LOG.info("佣金提现交易修复结束");
            return ApiResult.succ();
        }

        LOG.info("存在{}个待修复的数据", getRecords.size());
        Set<Long> userIdSet = new HashSet<>(getRecords.stream().map(
                PosUserGetBrokerageRecordDto::getUserId).collect(Collectors.toList()));
        List<CustomerPermission> permissions = repairV2_0_0Dao.getPermissions(Lists.newArrayList(userIdSet));
        if (permissions.size() != userIdSet.size()) {
            LOG.error("佣金提现中有不存在于系统中的用户");
            LOG.error("佣金提现交易修复异常结束");
            return ApiResult.fail(new ErrorCode() {
                @Override
                public int getCode() {
                    return -1;
                }

                @Override
                public String getMessage() {
                    return "佣金提现中有不存在于系统中的用户, 佣金提现交易修复异常结束";
                }
            });
        }
        Map<Long, CustomerPermission> permissionMap = new HashMap<>();
        permissions.forEach(e -> permissionMap.put(e.getUserId(), e));

        List<Long> succList = Lists.newArrayList();
        getRecords.forEach(e -> {
            UserPosTransactionRecord transaction = buildBrokerageTransaction(permissionMap.get(e.getUserId()), e);
            repairV2_0_0Dao.saveBrokerageTransaction(transaction);
            UserPosTransactionHandledInfo handledInfo = buildTransactionHandledInfo(transaction, e);
            repairV2_0_0Dao.saveTransactionHandled(handledInfo);
            succList.add(transaction.getId());
        });

        LOG.info("佣金提现交易修复结束，一共修复{}个数据", succList.size());
        return ApiResult.succ(succList);
    }

    /**
     * 构建一个交易手动处理记录
     *
     * @param transaction 佣金交易信息
     * @param record      旧佣金手动处理记录
     * @return 交易手动处理记录
     */
    private UserPosTransactionHandledInfo buildTransactionHandledInfo(
            UserPosTransactionRecord transaction, PosUserGetBrokerageRecordDto record) {
        UserPosTransactionHandledInfo handledInfo = new UserPosTransactionHandledInfo();
        handledInfo.setRecordId(transaction.getId());
        handledInfo.setAmount(record.getAmount());
        handledInfo.setPayMode(record.getPayMode());
        handledInfo.setVoucher(record.getVoucher());
        handledInfo.setRemark(record.getRemark());
        handledInfo.setCreateDate(record.getPayDate());
        handledInfo.setCreateUserId(10241024L);
        return handledInfo;
    }

    /**
     * 构建一个佣金提现交易
     *
     * @param permission 客户权限信息
     * @param record     旧佣金处理记录
     * @return 交易原始信息
     */
    private UserPosTransactionRecord buildBrokerageTransaction(
            CustomerPermission permission, PosUserGetBrokerageRecordDto record) {
        UserPosTransactionRecord transaction = new UserPosTransactionRecord();

        transaction.setRecordNum(UUIDUnsigned32.randomUUIDString());
        transaction.setTransactionType(TransactionType.BROKERAGE_WITHDRAW.getCode());
        transaction.setInCardId(permission.getPosCardId());
        transaction.setUserId(permission.getUserId());
        BigDecimal brokerage = record.getAmount();
        transaction.setAmount(brokerage);
        transaction.setArrivalAmount(brokerage);
        BigDecimal posCharge = brokerage
                .multiply(posConstants.getHelibaoTixianRate())
                .add(posConstants.getHelibaoTixianMoney())
                .setScale(2, BigDecimal.ROUND_UP);
        transaction.setPosCharge(posCharge);
        transaction.setStatus(TransactionStatusType.TRANSACTION_HANDLED_SUCCESS.getCode());
        transaction.setCreateDate(record.getPayDate());
        transaction.setCompleteDate(record.getPayDate());

        return transaction;
    }
}
