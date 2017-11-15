/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain.v1_0_0;

import java.io.Serializable;

/**
 * 用户验证信息领域对象
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class User implements Serializable {

    private Long id;

    private String loginName; // 登录账号名

    private String password; // 登录密码

    private String phone; // 用户绑定的手机号码(当为后台管理账号时，与登陆账号名相同)

    private Boolean enableStatus; // 账号启/禁用状态

    private String userType; // 用户类型

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnableStatus() {
        return enableStatus;
    }

    public boolean isEnable() {
        return enableStatus;
    }

    public void setEnableStatus(Boolean enableStatus) {
        this.enableStatus = enableStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
