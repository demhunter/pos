/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 推客信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
public class Twitter implements Serializable {

    private static final long serialVersionUID = 8608369154283610437L;

    private Long id;

    private Long userId; // 推客的userId

    private BigDecimal totalMoney; // 提现总金额

    private BigDecimal applyMoney; // 当前提现金额

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;

    @Deprecated
    private String remark; // 备注

    @Deprecated
    private Long parentUserId; // 上级推客的userId，为空表示没有上级推客

    @Deprecated
    private Boolean relationAvailable; // 上下级推客关系是否有效，当为false时表示解除上下级推客推客关系

    @Deprecated
    private String channelPhone; // 推客电话号码

    @Deprecated
    public Long getParentUserId() {
        return parentUserId;
    }

    @Deprecated
    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    @Deprecated
    public Boolean getRelationAvailable() {
        return relationAvailable;
    }

    @Deprecated
    public void setRelationAvailable(Boolean relationAvailable) {
        this.relationAvailable = relationAvailable;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Deprecated
    public String getChannelPhone() {
        return channelPhone;
    }

    @Deprecated
    public void setChannelPhone(String channelPhone) {
        this.channelPhone = channelPhone;
    }

    @Deprecated
    public String getRemark() {
        return remark;
    }

    @Deprecated
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
