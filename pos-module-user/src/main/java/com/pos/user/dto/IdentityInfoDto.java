/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用户登录或注册身份信息Dto
 *
 * @author wangbing
 * @version 1.0, 2016/12/16
 */
public class IdentityInfoDto implements Serializable {

    private static final long serialVersionUID = -4060540489670114623L;
    /**
     * 用户登录或注册用户名
     */
    @ApiModelProperty("用户登录或注册用户名")
    private String loginName;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;

    /**
     * 短信验证码
     */
    @ApiModelProperty("短信验证码")
    private String smsCode;

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
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
