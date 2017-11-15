/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto;

import com.pos.user.constant.UserType;

import java.io.Serializable;

/**
 * 用户登录DTO.
 *
 * @author wayne
 * @version 1.0, 2016/6/30
 */
public class UserLoginDto implements Serializable {

    private static final long serialVersionUID = 8611570436774857702L;

    private UserType userType; // 登录的用户类型

    private String loginName; // 登录名，根据userType可以是userName或者userPhone

    private String password; // 登录密码（不做MD5）

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
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

}