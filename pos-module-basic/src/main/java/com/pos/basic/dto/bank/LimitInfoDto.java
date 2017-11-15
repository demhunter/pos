/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.bank;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * LimitInfoDto
 *
 * @author cc
 * @version 1.0, 2017/1/9
 */
public class LimitInfoDto implements Serializable {

    /**
     * 单笔限额
     */
    @ApiModelProperty("单笔限额")
    private BigDecimal limitPerOp;

    /**
     * 单日限额
     */
    @ApiModelProperty("单日限额")
    private BigDecimal limitPerDay;

    /**
     * 单月限额
     */
    @ApiModelProperty("单月限额")
    private BigDecimal limitPerMonth;

    public LimitInfoDto() {
    }

    public LimitInfoDto(BigDecimal limitPerOp, BigDecimal limitPerDay, BigDecimal limitPerMonth) {
        this.limitPerOp = limitPerOp;
        this.limitPerDay = limitPerDay;
        this.limitPerMonth = limitPerMonth;
    }

    public BigDecimal getLimitPerDay() {
        return limitPerDay;
    }

    public void setLimitPerDay(BigDecimal limitPerDay) {
        this.limitPerDay = limitPerDay;
    }

    public BigDecimal getLimitPerMonth() {
        return limitPerMonth;
    }

    public void setLimitPerMonth(BigDecimal limitPerMonth) {
        this.limitPerMonth = limitPerMonth;
    }

    public BigDecimal getLimitPerOp() {
        return limitPerOp;
    }

    public void setLimitPerOp(BigDecimal limitPerOp) {
        this.limitPerOp = limitPerOp;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
