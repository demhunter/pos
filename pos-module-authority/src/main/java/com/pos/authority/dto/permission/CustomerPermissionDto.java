/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.permission;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 客户权限DTO
 *
 * @author wangbing
 * @version 1.0, 2017/11/24
 */
public class CustomerPermissionDto implements Serializable {

    private static final long serialVersionUID = 2566400720013890423L;

    @ApiModelProperty("权限主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户等级")
    private int level;

    @ApiModelProperty("用户收款权限：1 = 未启用，2 = 启用，3 = 关闭")
    private int withdrawPermission;

    @ApiModelProperty("用户收款费率")
    private BigDecimal withdrawRate;

    @ApiModelProperty("父用户id")
    private Long parentUserId;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getWithdrawPermission() {
        return withdrawPermission;
    }

    public void setWithdrawPermission(int withdrawPermission) {
        this.withdrawPermission = withdrawPermission;
    }

    public BigDecimal getWithdrawRate() {
        return withdrawRate;
    }

    public void setWithdrawRate(BigDecimal withdrawRate) {
        this.withdrawRate = withdrawRate;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }
}
