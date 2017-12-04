/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户统计
 *
 * @author wangbing
 * @version 1.0, 2017/12/4
 */
public class CustomerStatistics implements Serializable {

    private Long id; // 自增主键id

    private Long userId; // 用户id

    private BigDecimal paidCharge; // 晋升等级已支付的晋升服务费

    private Integer childrenCount; // 直接下级数量

    private BigDecimal withdrawAmount; // 收款总金额

    private BigDecimal totalBrokerage; // 总佣金

    private BigDecimal withdrawalBrokerage; // 已提现佣金

    private Integer withdrawalBrokerageTimes; // 佣金提现次数

    private Integer interviewTimes; // 回访次数

    private Date updateTime; // 更新时间

    public CustomerStatistics() {
    }

    public CustomerStatistics(Long userId) {
        this.userId = userId;
        this.paidCharge = BigDecimal.ZERO;
        this.childrenCount = 0;
        this.withdrawAmount = BigDecimal.ZERO;
        this.totalBrokerage = BigDecimal.ZERO;
        this.withdrawalBrokerage = BigDecimal.ZERO;
        this.withdrawalBrokerageTimes = 0;
        this.interviewTimes = 0;
    }

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
