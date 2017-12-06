/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.relation;

import com.pos.common.util.basic.Copyable;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户关联关系Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/4
 */
public class CustomerRelationDto implements Serializable, Copyable<CustomerRelationDto> {

    private static final long serialVersionUID = -4232221009446877688L;

    @ApiModelProperty("关系自增主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户等级")
    private Integer level;

    @ApiModelProperty("用户提现费率")
    private BigDecimal withdrawRate;

    @ApiModelProperty("用户提现额外手续费")
    private BigDecimal extraServiceCharge;

    @ApiModelProperty("用户身份认证状态")
    private Integer auditStatus;

    @ApiModelProperty("父用户id")
    private Long parentUserId;

    @ApiModelProperty("父用户对子用户的备注")
    private String remark;

    @ApiModelProperty("更新用户id")
    private Long updateUserId;

    @ApiModelProperty("更新操作时间")
    private Date updateTime;

    @ApiModelProperty("父子关系建立时间")
    private Date relationTime;

    @Override
    public CustomerRelationDto copy() {
        CustomerRelationDto backup = new CustomerRelationDto();
        BeanUtils.copyProperties(this, backup);
        return backup;
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

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(Date relationTime) {
        this.relationTime = relationTime;
    }
}
