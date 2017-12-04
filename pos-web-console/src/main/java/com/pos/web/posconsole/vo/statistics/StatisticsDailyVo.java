/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.posconsole.vo.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 每日统计信息
 *
 * @author wangbing
 * @version 1.0, 2017/12/3
 */
public class StatisticsDailyVo implements Serializable {

    @ApiModelProperty("日期")
    private Date dateKey;

    @ApiModelProperty("收款人数")
    private Long posCustomerCount;

    @ApiModelProperty("收款笔数")
    private Long posTransactionCount;

    @ApiModelProperty("收款总额")
    private BigDecimal posAmount;

    @ApiModelProperty("平台毛利润（BigDecimal）")
    private BigDecimal grossProfit;

    @ApiModelProperty("佣金提现人数")
    private Long brokerageCustomerCount;

    @ApiModelProperty("佣金提现次数")
    private Long brokerageWithdrawalTimes;

    @ApiModelProperty("佣金提现手续费支出（BigDecimal）")
    private BigDecimal brokerageServiceCharge;

    public Date getDateKey() {
        return dateKey;
    }

    public void setDateKey(Date dateKey) {
        this.dateKey = dateKey;
    }

    public Long getPosCustomerCount() {
        return posCustomerCount;
    }

    public void setPosCustomerCount(Long posCustomerCount) {
        this.posCustomerCount = posCustomerCount;
    }

    public Long getPosTransactionCount() {
        return posTransactionCount;
    }

    public void setPosTransactionCount(Long posTransactionCount) {
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

    public Long getBrokerageCustomerCount() {
        return brokerageCustomerCount;
    }

    public void setBrokerageCustomerCount(Long brokerageCustomerCount) {
        this.brokerageCustomerCount = brokerageCustomerCount;
    }

    public Long getBrokerageWithdrawalTimes() {
        return brokerageWithdrawalTimes;
    }

    public void setBrokerageWithdrawalTimes(Long brokerageWithdrawalTimes) {
        this.brokerageWithdrawalTimes = brokerageWithdrawalTimes;
    }

    public BigDecimal getBrokerageServiceCharge() {
        return brokerageServiceCharge;
    }

    public void setBrokerageServiceCharge(BigDecimal brokerageServiceCharge) {
        this.brokerageServiceCharge = brokerageServiceCharge;
    }
}
