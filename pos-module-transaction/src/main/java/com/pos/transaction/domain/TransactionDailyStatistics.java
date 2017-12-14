/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * POS每日统计数据领域对象
 *
 * @author wangbing
 * @version 1.0, 2017/12/13
 */
public class TransactionDailyStatistics implements Serializable {
    private static final long serialVersionUID = -3621920657609257479L;

    private Long id;

    private Date date; // 日期

    private Integer posCustomerCount; // 收款人数

    private Integer posTransactionCount; // 收款笔数

    private BigDecimal posAmount; // 收款金额

    private BigDecimal grossProfit; // 毛利润

    private BigDecimal brokerage; // 交易产生佣金

    private Integer brokerageCustomerCount; // 佣金提现人数

    private Integer brokerageWithdrawTimes; // 佣金提现次数

    private BigDecimal brokerageServiceCharge; // 佣金提现手续费

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPosCustomerCount() {
        return posCustomerCount;
    }

    public void setPosCustomerCount(Integer posCustomerCount) {
        this.posCustomerCount = posCustomerCount;
    }

    public Integer getPosTransactionCount() {
        return posTransactionCount;
    }

    public void setPosTransactionCount(Integer posTransactionCount) {
        this.posTransactionCount = posTransactionCount;
    }

    public BigDecimal getPosAmount() {
        return posAmount;
    }

    public void setPosAmount(BigDecimal posAmount) {
        this.posAmount = posAmount;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public Integer getBrokerageCustomerCount() {
        return brokerageCustomerCount;
    }

    public void setBrokerageCustomerCount(Integer brokerageCustomerCount) {
        this.brokerageCustomerCount = brokerageCustomerCount;
    }

    public Integer getBrokerageWithdrawTimes() {
        return brokerageWithdrawTimes;
    }

    public void setBrokerageWithdrawTimes(Integer brokerageWithdrawTimes) {
        this.brokerageWithdrawTimes = brokerageWithdrawTimes;
    }

    public BigDecimal getBrokerageServiceCharge() {
        return brokerageServiceCharge;
    }

    public void setBrokerageServiceCharge(BigDecimal brokerageServiceCharge) {
        this.brokerageServiceCharge = brokerageServiceCharge;
    }
}
