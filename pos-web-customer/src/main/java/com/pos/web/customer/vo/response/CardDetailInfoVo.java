/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.vo.response;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.pos.dto.CreateOrderDto;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class CardDetailInfoVo implements Serializable{

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("卡号")
    private String cardNO;

    @ApiModelProperty("下单的ID")
    private long id;

    public CardDetailInfoVo(){

    }

    public CardDetailInfoVo(CreateOrderDto createOrderDto) {
        BeanUtils.copyProperties(createOrderDto,this);
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
