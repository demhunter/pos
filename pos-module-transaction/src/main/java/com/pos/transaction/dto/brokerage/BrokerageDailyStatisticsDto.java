/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.brokerage;

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

    private static final long serialVersionUID = 1151499505026543976L;
    @ApiModelProperty("日期(date)")
    private Date dateKey;

    @ApiModelProperty("佣金金额（BigDecimal）")
    private BigDecimal brokerage;

    @ApiModelProperty("是否为月份统计（true：当前项为月份的统计）")
    private boolean monthStatistics;

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

    public Date getDateKey() {
        return dateKey;
    }

    public void setDateKey(Date dateKey) {
        this.dateKey = dateKey;
    }
}
