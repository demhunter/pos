/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.get;

import com.pos.pos.dto.card.PosCardDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 快捷收款信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/18
 */
public class QuickGetMoneyDto implements Serializable {

    @ApiModelProperty("手续费率（BigDecimal，具体数值如0.0058，返给前端是百分化的数值）")
    private BigDecimal poundageRate;

    @ApiModelProperty("手续费（BigDecimal，单位：元）")
    private BigDecimal poundage;

    @ApiModelProperty("到账时间")
    private String arrival;

    public void hundredPercentPoundageRate() {
        if (poundageRate != null) {
            this.poundageRate = this.poundageRate.multiply(new BigDecimal("100"));
        }
    }

    public BigDecimal getPoundageRate() {
        return poundageRate;
    }

    public void setPoundageRate(BigDecimal poundageRate) {
        this.poundageRate = poundageRate;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }
}
