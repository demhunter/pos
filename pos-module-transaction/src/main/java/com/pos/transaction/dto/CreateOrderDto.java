/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 下单成功信息Dto
 *
 * @author 睿智
 * @version 1.0, 2017/8/23
 */
public class CreateOrderDto implements Serializable{

    @ApiModelProperty("金额(BigDecimal，下单金额)")
    private BigDecimal amount;

    @ApiModelProperty("卡号")
    private String cardNO;

    @ApiModelProperty("下单的ID")
    private Long id;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
