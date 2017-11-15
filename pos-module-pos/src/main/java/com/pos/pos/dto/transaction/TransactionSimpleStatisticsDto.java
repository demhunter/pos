/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.transaction;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * POS 交易记录统计信息
 *
 * @author wangbing
 * @version 1.0, 2017/10/21
 */
public class TransactionSimpleStatisticsDto {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("交易数量")
    private Integer transactionCount;

    @ApiModelProperty("交易总金额")
    private BigDecimal transactionAmount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
