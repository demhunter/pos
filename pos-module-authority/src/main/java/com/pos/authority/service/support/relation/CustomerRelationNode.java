/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.support.relation;

import com.pos.authority.dto.relation.CustomerRelationDto;
import com.pos.common.util.basic.Copyable;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * 客户关系节点信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/24
 */
public class CustomerRelationNode implements Serializable, Comparable<CustomerRelationNode>, Copyable<CustomerRelationNode> {

    private static final long serialVersionUID = -6224694235401230733L;

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

    @ApiModelProperty("直接下级用户集合")
    private Set<Long> children;

    public CustomerRelationNode() {
        children = new HashSet<>();
    }

    public CustomerRelationNode(CustomerRelationDto relation) {
        this(relation, new HashSet<>());
    }

    public CustomerRelationNode(CustomerRelationDto relation, Set<Long> children) {
        this.userId = relation.getUserId();
        this.level = relation.getLevel();
        this.withdrawRate = relation.getWithdrawRate();
        this.extraServiceCharge = relation.getExtraServiceCharge();
        this.auditStatus = relation.getAuditStatus();
        this.parentUserId = relation.getParentUserId();
        this.relationTime = relation.getRelationTime();
        this.children = children;
    }

    @Override
    public CustomerRelationNode copy() {
        CustomerRelationNode backup = new CustomerRelationNode();
        BeanUtils.copyProperties(this, backup);
        backup.setChildren(new HashSet<>());
        return backup;
    }

    /**
     * 复制节点信息
     *
     * @param containDescendant 是否保留子树（子孙）信息
     * @return 节点信息
     */
    public CustomerRelationNode copyContainDescendant(boolean containDescendant) {
        CustomerRelationNode backup = new CustomerRelationNode();
        BeanUtils.copyProperties(this, backup);
        if (!containDescendant) {
            backup.setChildren(new HashSet<>());
        }
        return backup;
    }

    @Override
    public int compareTo(CustomerRelationNode o) {
        if (o == null) {
            return 0;
        }
        if (this.level < o.getLevel()) {
            return -1;
        } else if (this.level > o.getLevel()) {
            return 1;
        } else {
            return 0;
        }
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

    public Set<Long> getChildren() {
        return children;
    }

    public void setChildren(Set<Long> children) {
        this.children = children;
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

    public Date getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(Date relationTime) {
        this.relationTime = relationTime;
    }
}
