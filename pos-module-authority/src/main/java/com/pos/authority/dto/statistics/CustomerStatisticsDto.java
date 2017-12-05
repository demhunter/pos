/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户统计Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
public class CustomerStatisticsDto implements Serializable {

    private static final long serialVersionUID = -4564075124795676889L;

    @ApiModelProperty("自增主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("晋升等级已支付的晋升服务费")
    private BigDecimal paidCharge;

    @ApiModelProperty("直接下级数量")
    private Integer childrenCount;

    @ApiModelProperty("收款总金额")
    private BigDecimal withdrawAmount;

    @ApiModelProperty("总佣金")
    private BigDecimal totalBrokerage;

    @ApiModelProperty("已提现佣金")
    private BigDecimal withdrawalBrokerage;

    @ApiModelProperty("佣金提现次数")
    private Integer withdrawalBrokerageTimes;

    @ApiModelProperty("回访次数")
    private Integer interviewTimes;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getPaidCharge() {
        return paidCharge;
    }

    public void setPaidCharge(BigDecimal paidCharge) {
        this.paidCharge = paidCharge;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

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

    public Integer getWithdrawalBrokerageTimes() {
        return withdrawalBrokerageTimes;
    }

    public void setWithdrawalBrokerageTimes(Integer withdrawalBrokerageTimes) {
        this.withdrawalBrokerageTimes = withdrawalBrokerageTimes;
    }

    public Integer getInterviewTimes() {
        return interviewTimes;
    }

    public void setInterviewTimes(Integer interviewTimes) {
        this.interviewTimes = interviewTimes;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
