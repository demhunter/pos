/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户附加信息领域对象
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class UserExtension implements Serializable {

    private Long id;

    private Long userId; // 用户id

    private Date registerTime; // 注册时间

    private String registerInfo; // 注册信息(JSON)

    private Date lastLoginTime; // 最近一次登录时间

    private String lastLoginInfo; // 最近一次登录信息(JSON)

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

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegisterInfo() {
        return registerInfo;
    }

    public void setRegisterInfo(String registerInfo) {
        this.registerInfo = registerInfo;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginInfo() {
        return lastLoginInfo;
    }

    public void setLastLoginInfo(String lastLoginInfo) {
        this.lastLoginInfo = lastLoginInfo;
    }
}
