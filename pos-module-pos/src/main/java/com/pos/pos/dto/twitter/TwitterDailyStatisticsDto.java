/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.twitter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 推广快捷收款每日记录Dto
s *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
public class TwitterDailyStatisticsDto implements Serializable {

    @ApiModelProperty("日期(date)")
    private Date dateKey;

    // @ApiModelProperty("收款单数")
    @Deprecated
    @JsonIgnore
    private Integer orderCount;

    @ApiModelProperty("下级推客返佣奖金（BigDecimal）")
    private BigDecimal childTwitterBrokerage;

    @ApiModelProperty("客户返佣奖金（BigDecimal）")
    private BigDecimal customerBrokerage;

    @ApiModelProperty("累计奖金（BigDecimal，childTwitterBrokerage + customerBrokerage）")
    public BigDecimal getBrokerage() {
        return BigDecimal.ZERO
                .add(customerBrokerage == null ? BigDecimal.ZERO : customerBrokerage)
                .add(childTwitterBrokerage == null ? BigDecimal.ZERO : childTwitterBrokerage);
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

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
}
