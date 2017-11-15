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

    @ApiModelProperty("登录用户类型（前端不传此字段，由服务端填充）")
    private String userType;

    @ApiModelProperty("用户登录或注册用户名")
    private String loginName;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("短信验证码")
    private String smsCode;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

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
