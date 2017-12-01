/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.level;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 等级配置信息VO
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class LevelConfigVo implements Serializable {

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("相应等级的收款费率(BigDecimal)")
    private BigDecimal withdrawRate;

    @ApiModelProperty("相应等级的收款额外服务费(BigDecimal)")
    private BigDecimal extraServiceCharge;

    @ApiModelProperty("等级晋升支付费用限制(BigDecimal)")
    private BigDecimal chargeLimit;

    @ApiModelProperty("等级晋升直接下级数量限制")
    private Integer childrenLimit;

    @ApiModelProperty("等级晋升收款总金额限制(BigDecimal)")
    private BigDecimal withdrawAmountLimit;

    @ApiModelProperty("每1000万交易可赚的分润")
    private BigDecimal brokerage;

    @ApiModelProperty("是否为用户当前等级（true：为用户当前等级）")
    private boolean customerCurrentLevel;

    @ApiModelProperty("是否可以通过普通升级达到（false：咨询平台客服）")
    private boolean canUpgrade;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BigDecimal getWithdrawRate() {
        return withdrawRate;
    }

    public void setWithdrawRate(BigDecimal withdrawRate) {
        this.withdrawRate = withdrawRate;
    }

    public BigDecimal getExtraServiceCharge() {
        return extraServiceCharge;
    }

    public void setExtraServiceCharge(BigDecimal extraServiceCharge) {
        this.extraServiceCharge = extraServiceCharge;
    }

    public BigDecimal getChargeLimit() {
        return chargeLimit;
    }

    public void setChargeLimit(BigDecimal chargeLimit) {
        this.chargeLimit = chargeLimit;
    }

    public Integer getChildrenLimit() {
        return childrenLimit;
    }

    public void setChildrenLimit(Integer childrenLimit) {
        this.childrenLimit = childrenLimit;
    }

    public BigDecimal getWithdrawAmountLimit() {
        return withdrawAmountLimit;
    }

    public void setWithdrawAmountLimit(BigDecimal withdrawAmountLimit) {
        this.withdrawAmountLimit = withdrawAmountLimit;
    }

    public boolean isCustomerCurrentLevel() {
        return customerCurrentLevel;
    }

    public void setCustomerCurrentLevel(boolean customerCurrentLevel) {
        this.customerCurrentLevel = customerCurrentLevel;
    }

    public boolean isCanUpgrade() {
        return canUpgrade;
    }

    public void setCanUpgrade(boolean canUpgrade) {
        this.canUpgrade = canUpgrade;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }
}
