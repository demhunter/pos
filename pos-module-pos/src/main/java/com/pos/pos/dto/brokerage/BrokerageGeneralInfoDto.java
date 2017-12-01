/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.brokerage;

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
    private BigDecimal canApplyMoney;

    @ApiModelProperty("累计提现金额（BigDecimal）")
    private BigDecimal totalApplyMoney;

    @Deprecated
    @JsonIgnore
    private BigDecimal currentApplyMoney;

    @ApiModelProperty("今日收益（BigDecimal）")
    private BigDecimal todayBrokerage;

    public BigDecimal getTodayBrokerage() {
        return todayBrokerage;
    }

    public void setTodayBrokerage(BigDecimal todayBrokerage) {
        this.todayBrokerage = todayBrokerage;
    }

    public BigDecimal getCanApplyMoney() {
        return canApplyMoney;
    }

    public void setCanApplyMoney(BigDecimal canApplyMoney) {
        this.canApplyMoney = canApplyMoney;
    }

    public BigDecimal getTotalApplyMoney() {
        return totalApplyMoney;
    }

    public void setTotalApplyMoney(BigDecimal totalApplyMoney) {
        this.totalApplyMoney = totalApplyMoney;
    }

    public BigDecimal getCurrentApplyMoney() {
        return currentApplyMoney;
    }

    public void setCurrentApplyMoney(BigDecimal currentApplyMoney) {
        this.currentApplyMoney = currentApplyMoney;
    }
}
