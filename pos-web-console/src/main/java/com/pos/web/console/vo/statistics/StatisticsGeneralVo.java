/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.vo.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 统计信息总览
 *
 * @author wangbing
 * @version 1.0, 2017/12/3
 */
public class StatisticsGeneralVo implements Serializable {

    @ApiModelProperty("注册用户数")
    private Long customerCount;

    @ApiModelProperty("用户等级分布（Map集合，key为等级，value为对应等级的数量）")
    private Map<Integer, Long> levelDistribution;

    @ApiModelProperty("用户身份认证状态分布（Map集合，key为状态，value为对应状态的数量）")
    private Map<Integer, Long> auditStatusDistribution;

    @ApiModelProperty("已收款人数")
    private Long posCustomerCount;

    @ApiModelProperty("收款笔数")
    private Long posTransactionCount;

    @ApiModelProperty("收款总额（BigDecimal）")
    private BigDecimal totalPosAmount;

    @ApiModelProperty("平台毛利润（BigDecimal）")
    private BigDecimal grossProfit;

    @ApiModelProperty("佣金总额（BigDecimal）")
    private BigDecimal totalBrokerage;

    @ApiModelProperty("已提现佣金（BigDecimal）")
    private BigDecimal withdrawalBrokerage;

    @ApiModelProperty("未提现佣金（BigDecimal）")
    private BigDecimal noWithdrawalBrokerage;

    @ApiModelProperty("佣金提现人数")
    private Long brokerageCustomerCount;

    @ApiModelProperty("佣金提现次数")
    private Long brokerageWithdrawalTimes;

    @ApiModelProperty("佣金提现手续费支出（BigDecimal）")
    private BigDecimal brokerageServiceCharge;

    public BigDecimal getTotalBrokerage() {
        return totalBrokerage;
    }

    public void setTotalBrokerage(BigDecimal totalBrokerage) {
        this.totalBrokerage = totalBrokerage;
    }

    public BigDecimal getWithdrawalBrokerage() {
        return withdrawalBrokerage;
    }

    public void setWithdrawalBrokerage(BigDecimal withdrawalBrokerage) {
        this.withdrawalBrokerage = withdrawalBrokerage;
    }

    public BigDecimal getNoWithdrawalBrokerage() {
        return noWithdrawalBrokerage;
    }

    public void setNoWithdrawalBrokerage(BigDecimal noWithdrawalBrokerage) {
        this.noWithdrawalBrokerage = noWithdrawalBrokerage;
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

    public Long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Long customerCount) {
        this.customerCount = customerCount;
    }

    public Map<Integer, Long> getLevelDistribution() {
        return levelDistribution;
    }

    public void setLevelDistribution(Map<Integer, Long> levelDistribution) {
        this.levelDistribution = levelDistribution;
    }

    public Map<Integer, Long> getAuditStatusDistribution() {
        return auditStatusDistribution;
    }

    public void setAuditStatusDistribution(Map<Integer, Long> auditStatusDistribution) {
        this.auditStatusDistribution = auditStatusDistribution;
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

    public BigDecimal getTotalPosAmount() {
        return totalPosAmount;
    }

    public void setTotalPosAmount(BigDecimal totalPosAmount) {
        this.totalPosAmount = totalPosAmount;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }
}
