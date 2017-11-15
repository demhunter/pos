/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.spread;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // @ApiModelProperty("可提现余额（BigDecimal）")
    @Deprecated
    @JsonIgnore
    private BigDecimal canApplyMoney;

    // @ApiModelProperty("累计提现金额（BigDecimal）")
    @Deprecated
    @JsonIgnore
    private BigDecimal totalApplyMoney;

    // @ApiModelProperty("待处理提现金额（BigDecimal）")
    @Deprecated
    @JsonIgnore
    private BigDecimal currentApplyMoney;

    @ApiModelProperty("累计推广人数/发展的客户人数")
    private Integer spreadCount;

    // @ApiModelProperty("累计收款单数")
    @Deprecated
    @JsonIgnore
    private Integer spreadOrderCount;

    public SpreadGeneralInfoDto() {
    }

    public SpreadGeneralInfoDto(Long userId) {
        this.userId = userId;
    }

    // 初始化Null值字段
    public void initializeNull() {
        if (canApplyMoney == null) {
            canApplyMoney = BigDecimal.ZERO;
        }
        if (totalApplyMoney == null) {
            totalApplyMoney = BigDecimal.ZERO;
        }
        if (currentApplyMoney == null) {
            currentApplyMoney = BigDecimal.ZERO;
        }
        if (spreadOrderCount == null) {
            spreadOrderCount = 0;
        }
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

    public Integer getSpreadCount() {
        return spreadCount;
    }

    public void setSpreadCount(Integer spreadCount) {
        this.spreadCount = spreadCount;
    }

    public Integer getSpreadOrderCount() {
        return spreadOrderCount;
    }

    public void setSpreadOrderCount(Integer spreadOrderCount) {
        this.spreadOrderCount = spreadOrderCount;
    }
}
