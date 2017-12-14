/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 交易每日数据统计Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/13
 */
public class TransactionDailyStatisticsDto implements Serializable {
    private static final long serialVersionUID = -1827971698156807817L;

    @ApiModelProperty("日期")
    private Date dateKey;

    @ApiModelProperty("收款人数")
    private Integer posCustomerCount;

    @ApiModelProperty("收款笔数")
    private Integer posTransactionCount;

    @ApiModelProperty("收款总额")
    private BigDecimal posAmount;

    @ApiModelProperty("平台毛利润（BigDecimal）")
    private BigDecimal grossProfit;

    @ApiModelProperty("佣金提现人数")
    private Integer brokerageCustomerCount;

    @ApiModelProperty("佣金提现次数")
    private Integer brokerageWithdrawalTimes;

    @ApiModelProperty("佣金提现手续费支出（BigDecimal）")
    private BigDecimal brokerageServiceCharge;

    public TransactionDailyStatisticsDto() {
    }

    public TransactionDailyStatisticsDto(Date dateKey) {
        this.dateKey = dateKey;
        posCustomerCount = 0;
        posTransactionCount = 0;
        posAmount = BigDecimal.ZERO;
        grossProfit = BigDecimal.ZERO;
        brokerageCustomerCount = 0;
        brokerageWithdrawalTimes = 0;
        brokerageServiceCharge = BigDecimal.ZERO;
    }

    public static TransactionDailyStatisticsDto initialize(LocalDate localDate) {
        Date dateKey = Date.from(localDate.atTime(LocalTime.NOON).atZone(ZoneId.systemDefault()).toInstant());
        return new TransactionDailyStatisticsDto(dateKey);
    }

    public Date getDateKey() {
        return dateKey;
    }

    public LocalDate getLocalDateKey() {
        return LocalDateTime.ofInstant(dateKey.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    public void setDateKey(Date dateKey) {
        this.dateKey = dateKey;
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

    public Integer getBrokerageCustomerCount() {
        return brokerageCustomerCount;
    }

    public void setBrokerageCustomerCount(Integer brokerageCustomerCount) {
        this.brokerageCustomerCount = brokerageCustomerCount;
    }

    public Integer getBrokerageWithdrawalTimes() {
        return brokerageWithdrawalTimes;
    }

    public void setBrokerageWithdrawalTimes(Integer brokerageWithdrawalTimes) {
        this.brokerageWithdrawalTimes = brokerageWithdrawalTimes;
    }

    public BigDecimal getBrokerageServiceCharge() {
        return brokerageServiceCharge;
    }

    public void setBrokerageServiceCharge(BigDecimal brokerageServiceCharge) {
        this.brokerageServiceCharge = brokerageServiceCharge;
    }
}
