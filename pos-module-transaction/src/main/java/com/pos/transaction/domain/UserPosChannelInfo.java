/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * POS 推客信息
 *
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class UserPosChannelInfo implements Serializable {

    private static final long serialVersionUID = 8608369154283610437L;

    private Long id;

    @Deprecated
    private long userId; // 用户ID userId -> parentUserId

    private Long parentUserId; // 上级推客的userId，为空表示没有上级推客

    private Boolean relationAvailable; // 上下级推客关系是否有效，当为false时表示解除上下级推客推客关系

    @Deprecated
    private long channelId; // 渠道商ID channelId -> channelUserId

    private Long channelUserId; // 推客的userId

    private String channelPhone; // 推客电话号码

    private String channelRemark; // 备注

    private BigDecimal totalMoney; // 提现总金额

    private BigDecimal applyMoney; // 当前提现金额

    private Date createDate;

    private Long updateUserId;

    private Date updateDate;

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public Boolean getRelationAvailable() {
        return relationAvailable;
    }

    public void setRelationAvailable(Boolean relationAvailable) {
        this.relationAvailable = relationAvailable;
    }

    public Long getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Long channelUserId) {
        this.channelUserId = channelUserId;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
    public long getUserId() {
        return userId;
    }

    @Deprecated
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Deprecated
    public long getChannelId() {
        return channelId;
    }

    @Deprecated
    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getChannelPhone() {
        return channelPhone;
    }

    public void setChannelPhone(String channelPhone) {
        this.channelPhone = channelPhone;
    }

    public String getChannelRemark() {
        return channelRemark;
    }

    public void setChannelRemark(String channelRemark) {
        this.channelRemark = channelRemark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
