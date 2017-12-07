/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.support.relation;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 客户关系树
 *
 * @author wangbing
 * @version 1.0, 2017/11/27
 */
public class CustomerRelationTree implements Serializable {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户等级")
    private int level;

    @ApiModelProperty("用户收款费率")
    private BigDecimal withdrawRate;

    @ApiModelProperty("用户收款额外手续费")
    private BigDecimal extraServiceCharge;

    @ApiModelProperty("用户身份认证状态")
    private Integer auditStatus;

    @ApiModelProperty("父用户id")
    private Long parentUserId;

    @ApiModelProperty("父子关系建立时间")
    private Date relationTime;

    @ApiModelProperty("直接下级集合（子树集合）")
    private Map<Long, CustomerRelationTree> childrenTrees;

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

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public Date getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(Date relationTime) {
        this.relationTime = relationTime;
    }

    public Map<Long, CustomerRelationTree> getChildrenTrees() {
        return childrenTrees;
    }

    public void setChildrenTrees(Map<Long, CustomerRelationTree> childrenTrees) {
        this.childrenTrees = childrenTrees;
    }
}
