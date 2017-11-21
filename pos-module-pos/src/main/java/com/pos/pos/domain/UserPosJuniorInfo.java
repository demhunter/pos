/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * POS 推客发展的下级客户
 *
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class UserPosJuniorInfo implements Serializable {

    private static final long serialVersionUID = -3517427541156948577L;

    private Long id;

    @Deprecated
    private long userId; // userId -> channelUserId

    private Long channelUserId; // 上线推客的userId

    @Deprecated
    private long juniorId;//下级的userid juniorId -> juniorUserId

    private Long juniorUserId; // 下级客户的userId

    private String juniorPhone; // 下级客户的电话

    private String juniorRemark; // 下级客户的备注

    private Boolean relationAvailable; // 上下级关系是否有效，当为false时表示解除上下级推客客户关系

    private Date createDate;

    private Long updateUserId;

    private Date updateDate;

    public Long getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Long channelUserId) {
        this.channelUserId = channelUserId;
    }

    public Long getJuniorUserId() {
        return juniorUserId;
    }

    public void setJuniorUserId(Long juniorUserId) {
        this.juniorUserId = juniorUserId;
    }

    public Boolean getRelationAvailable() {
        return relationAvailable;
    }

    public void setRelationAvailable(Boolean relationAvailable) {
        this.relationAvailable = relationAvailable;
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
    public long getJuniorId() {
        return juniorId;
    }

    @Deprecated
    public void setJuniorId(long juniorId) {
        this.juniorId = juniorId;
    }

    public String getJuniorPhone() {
        return juniorPhone;
    }

    public void setJuniorPhone(String juniorPhone) {
        this.juniorPhone = juniorPhone;
    }

    public String getJuniorRemark() {
        return juniorRemark;
    }

    public void setJuniorRemark(String juniorRemark) {
        this.juniorRemark = juniorRemark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
