/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息DTO的抽象基类
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public abstract class UserDto implements Serializable {

    // 默认用户头像
    public static final String DEFAULT_HEAD_IMAGE = "http://o8nljewkg.bkt.clouddn.com/o_1btehjls1153n17t9rrkgl1e9ir.png?width=138&height=138";

    @ApiModelProperty("用户编号")
    private Long id;

    @ApiModelProperty("登录账号名，如果注册时使用的是手机号，则默认为手机号")
    private String loginName;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("用户绑定手机号（当为后台管理员时，与登录账号名相同）")
    private String phone;

    @ApiModelProperty("账号状态")
    private Boolean enableStatus;

    @ApiModelProperty("用户类型：c = C端用户，m = 后台管理员")
    private String userType;

    @ApiModelProperty("账号注册时间")
    private Date registerTime;

    @ApiModelProperty("账号最近一次登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "用户显示给其他人看到的名称")
    public abstract String getShowName();

    @ApiModelProperty(value = "用户显示给其他人看到的头像")
    public abstract String getShowHead();

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public void setEnableStatus(Boolean enableStatus) {
        this.enableStatus = enableStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
