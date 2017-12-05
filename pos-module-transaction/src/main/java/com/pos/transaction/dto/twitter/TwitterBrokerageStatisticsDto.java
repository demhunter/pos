/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.twitter;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 下级客户（或推客）为上级推客带来的佣金
 *
 * @author wangbing
 * @version 1.0, 2017/11/2
 */
public class TwitterBrokerageStatisticsDto implements Serializable {

    @ApiModelProperty("下级客户（或推客的userId）")
    private Long userId;

    @ApiModelProperty("佣金金额")
    private BigDecimal brokerage;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }
}
