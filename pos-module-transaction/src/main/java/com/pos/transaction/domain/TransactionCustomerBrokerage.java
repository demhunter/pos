/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易客户佣金信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class TransactionCustomerBrokerage implements Serializable {

    private static final long serialVersionUID = -5865043392389931415L;
    private Long id; // 自增主键id

    private Long transactionId; // 交易id

    private Long ancestorUserId; // 参与分佣的上级用户id

    private Integer level; // 参与分佣的用户当前等级

    private BigDecimal withdrawRate; // 参与分佣的用户当前收款费率

    private BigDecimal brokerageRate; // 生成佣金的费率

    private BigDecimal brokerage; // 参与分佣用户应得的佣金

    private Integer status; // 当前佣金的状态

    private Date statusTime; // 状态变更时间

    private Date createTime; // 佣金生成时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getAncestorUserId() {
        return ancestorUserId;
    }

    public void setAncestorUserId(Long ancestorUserId) {
        this.ancestorUserId = ancestorUserId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BigDecimal getWithdrawRate() {
        return withdrawRate;
    }

    public void setWithdrawRate(BigDecimal withdrawRate) {
        this.withdrawRate = withdrawRate;
    }

    public BigDecimal getBrokerageRate() {
        return brokerageRate;
    }

    public void setBrokerageRate(BigDecimal brokerageRate) {
        this.brokerageRate = brokerageRate;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(Date statusTime) {
        this.statusTime = statusTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
