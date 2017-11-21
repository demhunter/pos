/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import com.pos.user.dto.UserExtensionInfoDto;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户类型的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public class UserClass implements Serializable {

    private static final long serialVersionUID = 6836364562727591013L;

    private Long id;

    private Long userId; // User.id

    private String userType; // 用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员

    private boolean available; // 是否可用

    private Long createUserId; // 创建人UID，如果等于userId，表示自主注册

    private Date createTime; // 创建时间

    private Date updateTime; // 更新时间

    private Date lastLoginTime; // 最近一次登录时间

    /**
     * 注册IP地址
     */
    private String registerIp;

    private String registerAddress;

    /**
     * 最近一侧登录IP地址
     */
    private String lastLoginIp;

    private String loginAddress;

    /**
     * 注册信息（JSON字串，{@link UserExtensionInfoDto}）
     */
    private String registerInfo;

    /**
     * 最近一次登录信息（JSON字串，{@link UserExtensionInfoDto}）
     */
    private String lastLoginInfo;

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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getRegisterInfo() {
        return registerInfo;
    }

    public void setRegisterInfo(String registerInfo) {
        this.registerInfo = registerInfo;
    }

    public String getLastLoginInfo() {
        return lastLoginInfo;
    }

    public void setLastLoginInfo(String lastLoginInfo) {
        this.lastLoginInfo = lastLoginInfo;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getLoginAddress() {
        return loginAddress;
    }

    public void setLoginAddress(String loginAddress) {
        this.loginAddress = loginAddress;
    }
}