/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 客户等级配置
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class CustomerLevelConfig implements Serializable {

    private static final long serialVersionUID = -8134866350163939914L;

    private Long id;

    private Integer level; // 等级

    private BigDecimal withdrawRate; // 相应等级的收款费率

    private BigDecimal extraServiceCharge; // 相应等级的收款额外服务费

    private BigDecimal chargeLimit; // 等级晋升支付费用限制

    private Integer childrenLimit; // 等级晋升直接下级数量限制

    private BigDecimal withdrawAmountLimit; // 等级晋升收款总金额限制

    private Boolean available; // 当前等级是否启用

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
