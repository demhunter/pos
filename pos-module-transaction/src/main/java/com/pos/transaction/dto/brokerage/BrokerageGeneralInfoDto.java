/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.brokerage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 佣金简要统计信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class BrokerageGeneralInfoDto implements Serializable {

    @ApiModelProperty("可提现余额（BigDecimal）")
    private BigDecimal canApplyBrokerage;

    @ApiModelProperty("累计提现金额（BigDecimal）")
    private BigDecimal appliedBrokerage;

    @Deprecated
    @JsonIgnore
    private BigDecimal currentApplyMoney;

    @ApiModelProperty("今日收益（BigDecimal）")
    private BigDecimal todayBrokerage;

    public BrokerageGeneralInfoDto() {
        canApplyBrokerage = BigDecimal.ZERO;
        appliedBrokerage = BigDecimal.ZERO;
        todayBrokerage = BigDecimal.ZERO;
    }

    public BigDecimal getTodayBrokerage() {
        return todayBrokerage;
    }

    public void setTodayBrokerage(BigDecimal todayBrokerage) {
        this.todayBrokerage = todayBrokerage;
    }

    public BigDecimal getCanApplyBrokerage() {
        return canApplyBrokerage;
    }

    public void setCanApplyBrokerage(BigDecimal canApplyBrokerage) {
        this.canApplyBrokerage = canApplyBrokerage;
    }

    public BigDecimal getAppliedBrokerage() {
        return appliedBrokerage;
    }

    public void setAppliedBrokerage(BigDecimal appliedBrokerage) {
        this.appliedBrokerage = appliedBrokerage;
    }

    public BigDecimal getCurrentApplyMoney() {
        return currentApplyMoney;
    }

    public void setCurrentApplyMoney(BigDecimal currentApplyMoney) {
        this.currentApplyMoney = currentApplyMoney;
    }
}
