/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.data.repair.v2_0_0;

import com.google.common.collect.Lists;
import com.pos.authority.domain.CustomerPermission;
import com.pos.authority.domain.CustomerRelation;
import com.pos.authority.domain.CustomerStatistics;
import com.pos.basic.constant.RedisConstants;
import com.pos.common.util.basic.UUIDUnsigned32;
import com.pos.common.util.exception.ErrorCode;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.data.repair.dao.RepairV2_0_0Dao;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.constants.TransactionStatusType;
import com.pos.transaction.constants.TransactionType;
import com.pos.transaction.domain.TransactionCustomerBrokerage;
import com.pos.transaction.domain.UserPosTransactionHandledInfo;
import com.pos.transaction.domain.UserPosTransactionRecord;
import com.pos.transaction.domain.UserPosTwitterBrokerage;
import com.pos.transaction.dto.PosUserGetBrokerageRecordDto;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
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

    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    // 修复用户统计数据
    public void repairCustomerStatistics() {
        LOG.info("开始修复用户统计数据......");

        LOG.info("获取用户统计数据......");
        List<CustomerStatistics> statisticsList = repairV2_0_0Dao.queryAllCustomerStatistics();
        if (CollectionUtils.isEmpty(statisticsList)) {
            LOG.info("没有用户统计数据");
            LOG.info("用户统计数据修复结束......");
            return;
        }

        Map<Long, CustomerStatistics> statisticsMap = new HashMap<>();
        statisticsList.forEach(e -> statisticsMap.put(e.getUserId(), e));

        LOG.info("修复用户直接下级统计数据......");
        List<CustomerRelation> relations = repairV2_0_0Dao.queryAllRelation();
        if (CollectionUtils.isEmpty(relations)) {
            LOG.info("没有用户关系数据");
        } else {
            LOG.info("共查询到{}条关系数据", relations.size());
            Map<Long, Integer> childCountMap = new HashMap<>();
            relations.forEach(e -> {
                if (e.getParentUserId() != 0) {
                    Integer count = childCountMap.get(e.getParentUserId());
                    if (count == null) {
                        count = 0;
                    }
                    childCountMap.put(e.getParentUserId(), count + 1);
                }
            });
            if (!CollectionUtils.isEmpty(childCountMap)) {
                childCountMap.forEach((userId, childCount) -> {
                    statisticsMap.get(userId).setChildrenCount(childCount);
                });
            }
            LOG.info("共修复{}条直接下级统计数据", childCountMap.size());
        }
        LOG.info("修复用户直接下级统计数据结束");

        LOG.info("修复用户收款统计数据......");
        List<UserPosTransactionRecord> normalTransactions = repairV2_0_0Dao.queryNormalTransaction();
        if (CollectionUtils.isEmpty(normalTransactions)) {
            LOG.info("没有用户收款数据");
        } else {
            LOG.info("共查询到{}条收款数据", normalTransactions.size());
            Map<Long, Integer> posCountMap = new HashMap<>();
            Map<Long, BigDecimal> posAmountMap = new HashMap<>();
            normalTransactions.forEach(e -> {
                Integer count = posCountMap.get(e.getUserId());
                if (count == null) {
                    count = 0;
                }
                posCountMap.put(e.getUserId(), count + 1);

                BigDecimal amount = posAmountMap.get(e.getUserId());
                if (amount == null) {
                    amount = BigDecimal.ZERO;
                }
                posAmountMap.put(e.getUserId(), amount.add(e.getAmount()));
            });
            if (!CollectionUtils.isEmpty(posCountMap)) {
                posCountMap.forEach((userId, count) -> {
                    CustomerStatistics statistics = statisticsMap.get(userId);
                    statistics.setWithdrawAmountTimes(count);
                    statistics.setWithdrawAmount(posAmountMap.get(userId));
                });
            }
            LOG.info("共修复{}条收款统计数据", posCountMap.size());
        }
        LOG.info("修复用户收款统计数据结束");

        LOG.info("修复用户总佣金统计数据......");
        List<TransactionCustomerBrokerage> brokerages = repairV2_0_0Dao.queryAllCustomerBrokerage();
        if (CollectionUtils.isEmpty(brokerages)) {
            LOG.info("没有交易分佣数据");
        } else {
            LOG.info("共查询到{}条交易分佣数据", brokerages.size());
            Map<Long, BigDecimal> brokerageMap = new HashMap<>();
            brokerages.forEach(e -> {
                BigDecimal brokerage = brokerageMap.get(e.getAncestorUserId());
                if (brokerage == null) {
                    brokerage = BigDecimal.ZERO;
                }
                brokerageMap.put(e.getAncestorUserId(), brokerage.add(e.getBrokerage()));
            });
            if (!CollectionUtils.isEmpty(brokerageMap)) {
                brokerageMap.forEach((userId, brokerage) -> {
                    statisticsMap.get(userId).setTotalBrokerage(brokerage);
                });
            }
            LOG.info("共修复{}条总佣金统计数据", brokerageMap.size());
        }
        LOG.info("修复用户总佣金统计数据结束");

        LOG.info("修复用户佣金提取统计数据......");
        List<UserPosTransactionRecord> brokerageTransactions = repairV2_0_0Dao.queryBrokerageTransaction();
        if (CollectionUtils.isEmpty(brokerageTransactions)) {
            LOG.info("没有佣金提取数据");
        } else {
            LOG.info("共查询到{}条佣金提取数据", brokerageTransactions.size());
            Map<Long, Integer> withdrawalTimesMap = new HashMap<>();
            Map<Long, BigDecimal> withdrawalBrokerageMap = new HashMap<>();
            brokerageTransactions.forEach(e -> {
                Integer times = withdrawalTimesMap.get(e.getUserId());
                if (times == null) {
                    times = 0;
                }
                withdrawalTimesMap.put(e.getUserId(), times + 1);
                BigDecimal brokerage = withdrawalBrokerageMap.get(e.getUserId());
                if (brokerage == null) {
                    brokerage = BigDecimal.ZERO;
                }
                withdrawalBrokerageMap.put(e.getUserId(), brokerage.add(e.getAmount()));
            });
            if (!CollectionUtils.isEmpty(withdrawalTimesMap)) {
                withdrawalTimesMap.forEach((userId, count) -> {
                    CustomerStatistics statistics = statisticsMap.get(userId);
                    statistics.setWithdrawalBrokerageTimes(count);
                    statistics.setWithdrawalBrokerage(withdrawalBrokerageMap.get(userId));
                });
            }
            LOG.info("共修复{}条佣金提取统计数据", withdrawalTimesMap.size());
        }
        LOG.info("修复用户佣金提取统计数据结束");

        statisticsList.forEach(e -> {
            repairV2_0_0Dao.updateCustomerStatistics(e);
        });
        LOG.info("用户统计数据修复结束......");
    }

    // 修复交易分佣记录
    public void repairTransactionCustomerBrokerage() {
        LOG.info("开始修复交易分佣记录......");
        List<UserPosTwitterBrokerage> twitterBrokerages = repairV2_0_0Dao.queryAllTwitterBrokerage();
        if (CollectionUtils.isEmpty(twitterBrokerages)) {
            LOG.info("没有待修复的交易分佣记录");
            LOG.info("交易分佣记录修复结束");
            return;
        }
        LOG.info("存在{}个待修复的数据", twitterBrokerages.size());
        Set<Long> userIdSet = new HashSet<>();
        twitterBrokerages.forEach(e -> {
            userIdSet.add(e.getAgentUserId());
            if (e.getParentAgentUserId() != null && e.getParentAgentUserId() != 0) {
                userIdSet.add(e.getParentAgentUserId());
            }
        });
        List<CustomerPermission> permissions = repairV2_0_0Dao.getPermissions(Lists.newArrayList(userIdSet));
        if (permissions.size() != userIdSet.size()) {
            LOG.error("交易分佣记录中有不存在于系统中的用户");
            LOG.error("交易分佣记录修复异常结束");
            return;
        }
        Map<Long, CustomerPermission> permissionMap = new HashMap<>();
        permissions.forEach(e -> permissionMap.put(e.getUserId(), e));
        List<TransactionCustomerBrokerage> brokerages = Lists.newArrayList();
        twitterBrokerages.forEach(e -> {
            TransactionCustomerBrokerage brokerage = new TransactionCustomerBrokerage();
            brokerage.setTransactionId(e.getRecordId());
            brokerage.setAncestorUserId(e.getAgentUserId());
            CustomerPermission permission = permissionMap.get(e.getAgentUserId());
            brokerage.setLevel(permission.getLevel());
            brokerage.setWithdrawRate(permission.getWithdrawRate());
            brokerage.setBrokerageRate(e.getAgentRate());
            brokerage.setBrokerage(e.getAgentCharge());
            brokerage.setStatus(e.getGetAgent().intValue());
            Date statusTime = e.getGetAgentDate();
            brokerage.setStatusTime(statusTime == null ? e.getCreateDate() : statusTime);
            brokerage.setCreateTime(e.getCreateDate());

            brokerages.add(brokerage);
            if (e.getParentAgentUserId() != null && e.getParentAgentUserId() != 0) {
                TransactionCustomerBrokerage parentBrokerage = new TransactionCustomerBrokerage();
                parentBrokerage.setTransactionId(e.getRecordId());
                parentBrokerage.setAncestorUserId(e.getParentAgentUserId());
                CustomerPermission parent = permissionMap.get(e.getParentAgentUserId());
                parentBrokerage.setLevel(parent.getLevel());
                parentBrokerage.setWithdrawRate(parent.getWithdrawRate());
                parentBrokerage.setBrokerageRate(e.getParentAgentRate());
                parentBrokerage.setBrokerage(e.getParentAgentCharge());
                parentBrokerage.setStatus(e.getGetParentAgent().intValue());
                Date parentStatusTime = e.getGetParentDate();
                parentBrokerage.setStatusTime(parentStatusTime == null ? e.getCreateDate() : parentStatusTime);
                parentBrokerage.setCreateTime(e.getCreateDate());

                brokerages.add(parentBrokerage);
            }
        });

        repairV2_0_0Dao.saveCustomerBrokerages(brokerages);

        LOG.info("交易分佣记录修复结束，一共修复{}个数据", brokerages.size());
    }

    // 修复佣金提现交易
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

    public ApiResult<NullObject> clearRedisCache() {

        String redisKey = RedisConstants.POS_CUSTOMER_RELATION_NODE;
        Set<Serializable> keys = redisTemplate.opsForValue().getOperations().keys(redisKey + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            System.out.println("keys = " + keys);
            for (Serializable key : keys) {
                String keyStr = (String) key;
                String[] keyModels = Strings.split(keyStr, ':');
                Long userId = Long.valueOf(keyModels[1]);
                redisTemplate.opsForValue().getOperations().delete(RedisConstants.POS_CUSTOMER_RELATION_NODE_CHILDREN + userId);
                if (userId != 0L) {
                    redisTemplate.opsForValue().getOperations().delete(keyStr);
                }
            }
        }

        return ApiResult.succ();
    }
}
