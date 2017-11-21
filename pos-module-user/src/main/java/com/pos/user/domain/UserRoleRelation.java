/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import java.io.Serializable;

/**
 * 用户-角色关联领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/6/2
 */
public class UserRoleRelation implements Serializable {

    private static final long serialVersionUID = -7086254764482793321L;

    private Long id;

    private Long userId;

    private Long roleId;

    private boolean available; // 是否可用

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}