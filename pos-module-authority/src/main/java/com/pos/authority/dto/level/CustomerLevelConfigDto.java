/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.level;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 客户等级配置Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
public class CustomerLevelConfigDto implements Serializable, Comparable<CustomerLevelConfigDto> {

    // 计算分润佣金的基础金额
    private final static BigDecimal BASIC_POS_AMOUNT = new BigDecimal("10000000.00");
    private static final long serialVersionUID = 2958430855483946490L;

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

    @ApiModelProperty("是否可以通过普通升级达到（false：不能通过普通升级达到，需要咨询平台客服）")
    public boolean getCanUpgrade() {
        boolean isLimitCharge = chargeLimit != null && chargeLimit.compareTo(BigDecimal.ZERO) > 0;
        boolean isLimitChildren = childrenLimit != null && childrenLimit.compareTo(0) > 0;
        boolean isLimitWithdrawAmount = withdrawAmountLimit != null && withdrawAmountLimit.compareTo(BigDecimal.ZERO) > 0;

        return isLimitCharge && isLimitChildren && isLimitWithdrawAmount;
    }


    /**
     * 计算每1000万交易可赚的分润
     *
     * @param basicRate 基准费率
     */
    public void calculateBrokerage(BigDecimal basicRate) {
        BigDecimal differenceRate = basicRate.subtract(withdrawRate);
        if (differenceRate.compareTo(BigDecimal.ZERO) < 0) {
            this.brokerage = BigDecimal.ZERO;
        }
        this.brokerage = BASIC_POS_AMOUNT.multiply(differenceRate, new MathContext(2, RoundingMode.FLOOR));
    }

    @Override
    public int compareTo(CustomerLevelConfigDto o) {
        if (o == null) {
            return 0;
        }
        if (this.level < o.getLevel()) {
            return -1;
        } else if (this.level > o.getLevel()){
            return 1;
        }
        return 0;
    }

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

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public boolean isCustomerCurrentLevel() {
        return customerCurrentLevel;
    }

    public void setCustomerCurrentLevel(boolean customerCurrentLevel) {
        this.customerCurrentLevel = customerCurrentLevel;
    }
}
