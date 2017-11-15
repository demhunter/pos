/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 推客发展的下级客户
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
public class TwitterCustomer implements Serializable {

    private static final long serialVersionUID = -3517427541156948577L;

    private Long id;

    @Deprecated
    private Long twitterId; // 上线推客id

    private Long twitterUserId; // 上线推客userId

    private Long userId; // 下级客户的userId

    private String remark; // 下级客户的备注

    private Boolean available; // 上下级关系是否有效，当为false时表示解除上下级推客客户关系

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Deprecated
    public Long getTwitterId() {
        return twitterId;
    }

    @Deprecated
    public void setTwitterId(Long twitterId) {
        this.twitterId = twitterId;
    }

    public Long getTwitterUserId() {
        return twitterUserId;
    }

    public void setTwitterUserId(Long twitterUserId) {
        this.twitterUserId = twitterUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
}
