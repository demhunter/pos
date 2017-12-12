/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.auth;

import com.pos.transaction.constants.AuthStatusEnum;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.transaction.constants.PosTwitterStatus;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 基础权限信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class BaseAuthDto implements Serializable {

    private static final long serialVersionUID = 7151955630766129494L;
    @ApiModelProperty("收款功能（1=未启用 2=启用 3==关闭）")
    private Integer get;

    @ApiModelProperty("收款费率（BigDecimal，具体数值，如0.0045）")
    private BigDecimal getRate;

    @ApiModelProperty("手续费（BigDecimal，单位：元）")
    private BigDecimal poundage;

    @ApiModelProperty("推客状态（1=未启用 2=启用 3==关闭，限制下面两个推广功能，当推客状态为启用时，功能才能正常使用）")
    private Integer twitterStatus;

    @ApiModelProperty("推广发展客下级客户功能（1=未启用 2=启用 3==关闭）")
    private Integer spread;

    @ApiModelProperty("推广发展下级推客功能（1=未启用 2=启用 3==关闭）")
    private Integer develop;



    public AuthStatusEnum parseSpreadAuth() {
        return spread == null ? null : AuthStatusEnum.getEnum(spread.byteValue());
    }

    public AuthStatusEnum parseDevelopAuth() {
        return develop == null ? null : AuthStatusEnum.getEnum(develop.byteValue());
    }

    public PosTwitterStatus parseTwitterStatus() {
        return twitterStatus == null ? null : PosTwitterStatus.getEnum(twitterStatus);
    }

    public AuthStatusEnum parseGetAuth() {
        return get == null ? null : AuthStatusEnum.getEnum(get.byteValue());
    }

    public Integer getGet() {
        return get;
    }

    public void setGet(Integer get) {
        this.get = get;
    }

    public BigDecimal getGetRate() {
        return getRate;
    }

    public void setGetRate(BigDecimal getRate) {
        this.getRate = getRate;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public Integer getTwitterStatus() {
        return twitterStatus;
    }

    public void setTwitterStatus(Integer twitterStatus) {
        this.twitterStatus = twitterStatus;
    }

    public Integer getSpread() {
        return spread;
    }

    public void setSpread(Integer spread) {
        this.spread = spread;
    }

    public Integer getDevelop() {
        return develop;
    }

    public void setDevelop(Integer develop) {
        this.develop = develop;
    }
}
