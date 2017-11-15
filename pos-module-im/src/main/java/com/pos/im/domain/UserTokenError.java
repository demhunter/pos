/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Description.
 *
 * @author wayne
 * @version 1.0, 2017/4/20
 */
public class UserTokenError implements Serializable {

    private static final long serialVersionUID = -4040476387822424294L;

    private Long id;

    private Long userId;

    private String userType;

    private String imKey;

    private String imUserId;

    private String imToken;

    private String message;

    private Date createTime;

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

    public String getImKey() {
        return imKey;
    }

    public void setImKey(String imKey) {
        this.imKey = imKey;
    }

    public String getImUserId() {
        return imUserId;
    }

    public void setImUserId(String imUserId) {
        this.imUserId = imUserId;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}