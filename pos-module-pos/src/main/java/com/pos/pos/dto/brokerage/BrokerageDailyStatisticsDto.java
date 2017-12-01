/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.brokerage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 推广快捷收款每日记录Dto
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
public class BrokerageDailyStatisticsDto implements Serializable {

    @ApiModelProperty("日期(date)")
    private Date dateKey;

    @ApiModelProperty("佣金金额（BigDecimal）")
    private BigDecimal brokerage;

    @ApiModelProperty("是否为月份统计（true：当前项为月份的统计）")
    private boolean monthStatistics;

    @Deprecated
    @JsonIgnore
    private BigDecimal childTwitterBrokerage;

    @Deprecated
    @JsonIgnore
    private BigDecimal customerBrokerage;

    public boolean isMonthStatistics() {
        return monthStatistics;
    }

    public void setMonthStatistics(boolean monthStatistics) {
        this.monthStatistics = monthStatistics;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public BigDecimal getChildTwitterBrokerage() {
        return childTwitterBrokerage;
    }

    public void setChildTwitterBrokerage(BigDecimal childTwitterBrokerage) {
        this.childTwitterBrokerage = childTwitterBrokerage;
    }

    public BigDecimal getCustomerBrokerage() {
        return customerBrokerage;
    }

    public void setCustomerBrokerage(BigDecimal customerBrokerage) {
        this.customerBrokerage = customerBrokerage;
    }

    public Date getDateKey() {
        return dateKey;
    }

    public void setDateKey(Date dateKey) {
        this.dateKey = dateKey;
    }
}
