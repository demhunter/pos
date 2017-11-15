/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.domain;

import java.util.Date;

/**
 * 推客关联关系领域对象
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
public class TwitterRelation {

    private Long id;

    private Long parentTwitterId; // 父推客id

    private Long twitterId; // 子推客id

    private Boolean available; // 推客推客关联关系是否有效

    private String remark; // 父推客对子推客的备注

    private Date createTime;

    private Date updateTime;

    private Long updateUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentTwitterId() {
        return parentTwitterId;
    }

    public void setParentTwitterId(Long parentTwitterId) {
        this.parentTwitterId = parentTwitterId;
    }

    public Long getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(Long twitterId) {
        this.twitterId = twitterId;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
}
