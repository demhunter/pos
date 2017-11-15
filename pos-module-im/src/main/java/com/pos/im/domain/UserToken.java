/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户IM Token的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/7/7
 */
public class UserToken implements Serializable {

    private static final long serialVersionUID = -7590197176867718745L;

    private Long id;

    private Long userId;

    private String userType; // 用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员

    private String token;

    private Date createTime;

    private boolean available;

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}