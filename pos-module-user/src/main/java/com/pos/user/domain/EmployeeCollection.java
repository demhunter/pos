/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import com.pos.common.util.basic.PrintableBeanUtils;

import java.util.Date;

/**
 * 业者-用户收藏领域对象
 *
 * @author wangbing
 * @version 1.0, 2016/12/29
 */
public class EmployeeCollection {

    private Long id; // 主键ID

    private Long employeeId; // 被收藏业者用户ID

    private Long userId; // 收藏用户ID

    private String userType; // 收藏用户类型

    private boolean available; // 收藏状态

    private Date createTime; // 创建时间（收藏时间）

    private Date updateTime; // 更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
