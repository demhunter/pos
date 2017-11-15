/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import com.pos.basic.dto.UserIdentifier;

import java.io.Serializable;
import java.util.Date;

/**
 * IM用户常用语的领域对象.
 *
 * @author wayne
 * @version 1.0, 2017/5/2
 */
public class CommonLanguage implements Serializable {

    private Long id;

    private Long userId;

    private String userType;

    private String content; // 用户的常用问题/回复

    private Date createTime;

    private Date updateTime;

    private Boolean available;

    public boolean isOwner(UserIdentifier user) {
        return userId.equals(user.getUserId()) && userType.equals(user.getUserType());
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

}