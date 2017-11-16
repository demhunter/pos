/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.develop;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 渠道商用户信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/8/24
 */
public class TwitterDto extends SimpleTwitterDto implements Serializable {

    @ApiModelProperty("累计已提现金额")
    private BigDecimal totalWithdrawDeposit;

    @ApiModelProperty("当前申请提现金额")
    private BigDecimal currentWithdrawDeposit;

    @ApiModelProperty("累计奖金，BigDecimal")
    private BigDecimal totalDeposit;

    public BigDecimal getTotalDeposit() {
        return totalDeposit == null ? BigDecimal.ZERO : totalDeposit;
    }

    public void setTotalDeposit(BigDecimal totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public BigDecimal getTotalWithdrawDeposit() {
        return totalWithdrawDeposit;
    }

    public void setTotalWithdrawDeposit(BigDecimal totalWithdrawDeposit) {
        this.totalWithdrawDeposit = totalWithdrawDeposit;
    }

    public BigDecimal getCurrentWithdrawDeposit() {
        return currentWithdrawDeposit;
    }

    public void setCurrentWithdrawDeposit(BigDecimal currentWithdrawDeposit) {
        this.currentWithdrawDeposit = currentWithdrawDeposit;
    }
}
