/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.spread;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发展收款客户概要信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
public class SpreadGeneralInfoDto implements Serializable {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("佣金费率（BigDecimal，返回具体数值，如万分之13，则返回0.0013）")
    private BigDecimal rate;

    @ApiModelProperty("累计推广人数/发展的客户人数")
    private Integer spreadCount;

    public SpreadGeneralInfoDto() {
    }

    public SpreadGeneralInfoDto(Long userId) {
        this.userId = userId;
    }

    // 初始化Null值字段
    public void initializeNull() {
        if (spreadCount == null) {
            spreadCount = 0;
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getSpreadCount() {
        return spreadCount;
    }

    public void setSpreadCount(Integer spreadCount) {
        this.spreadCount = spreadCount;
    }
}
