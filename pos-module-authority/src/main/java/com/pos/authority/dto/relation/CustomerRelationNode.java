/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.relation;

import com.pos.common.util.basic.Copyable;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户关系DTO
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

    @ApiModelProperty("用户收款权限：1 = 未启用，2 = 启用，3 = 关闭")
    private int withdrawPermission;

    @ApiModelProperty("用户收款费率")
    private BigDecimal withdrawRate;

    @ApiModelProperty("父用户id")
    private Long parentUserId;

    @ApiModelProperty("直接下级用户集合")
    private Map<Long, CustomerRelationNode> children;

    public CustomerRelationNode() {
        children = new HashMap<>();
    }

    @Override
    public CustomerRelationNode copy() {
        CustomerRelationNode backup = new CustomerRelationNode();
        BeanUtils.copyProperties(this, backup);
        backup.setChildren(new HashMap<>());
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

    public Map<Long, CustomerRelationNode> getChildren() {
        return children;
    }

    public void setChildren(Map<Long, CustomerRelationNode> children) {
        this.children = children;
    }
}
