/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.permission;

import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 客户权限基础信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
public class CustomerPermissionBasicDto implements Serializable {

    private static final long serialVersionUID = -1289728022065600921L;

    @ApiModelProperty("自增主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户等级")
    private Integer level;

    @ApiModelProperty("用户提现费率")
    private BigDecimal withdrawRate;

    @ApiModelProperty("用户提现额外手续费")
    private BigDecimal extraServiceCharge;

    public void check(String fieldPrefix) throws ValidationException {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(userId, fieldPrefix + "userId");
        FieldChecker.checkEmpty(level, fieldPrefix + "level");
        FieldChecker.checkEmpty(withdrawRate, fieldPrefix + "withdrawRate");
        FieldChecker.checkEmpty(extraServiceCharge, fieldPrefix + "extraServiceCharge");
    }

    public void hundredPercentRate() {
        if (this.withdrawRate != null) {
            this.withdrawRate = this.withdrawRate.multiply(new BigDecimal("100"));
        }
    }

    public void tenThousandPercentRate() {
        if (this.withdrawRate != null) {
            this.withdrawRate = this.withdrawRate.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_DOWN);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
