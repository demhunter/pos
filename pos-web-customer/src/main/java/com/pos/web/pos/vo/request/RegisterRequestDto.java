/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.request;

import com.pos.user.dto.UserExtensionInfoDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class RegisterRequestDto implements Serializable {

    private static final long serialVersionUID = -4660480413685948476L;
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("短信验证码")
    private String smsCode;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("注册类型（int，1 = APP注册（自动登录），2 = 分享注册（不自动登录），默认APP注册）")
    private Byte type = 1;

    @ApiModelProperty("选填，上一级的userId")
    private Long leaderId;

    @ApiModelProperty("用户注册拓展信息")
    private UserExtensionInfoDto userExtensionInfo;

    /**
     * 注册时是否自动登录和保存登录信息
     *
     * @return true：记录登录信息并自动登录；false：不记录登录信息且不登录
     */
    public boolean autoLogin() {
        return type != null && type == 1;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserExtensionInfoDto getUserExtensionInfo() {
        return userExtensionInfo;
    }

    public void setUserExtensionInfo(UserExtensionInfoDto userExtensionInfo) {
        this.userExtensionInfo = userExtensionInfo;
    }
}
