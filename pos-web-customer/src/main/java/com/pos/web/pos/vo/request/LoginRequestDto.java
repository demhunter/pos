/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.request;

import com.pos.user.dto.UserExtensionInfoDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 收款登陆信息
 *
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class LoginRequestDto implements Serializable {

    private static final long serialVersionUID = -7918839686436084148L;
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("登录的类型（int，1 = 推广发展客户的链接，2 = 推广发展渠推客的链接）")
    private Byte type;

    @ApiModelProperty("上一级的userId")
    private Long leaderId;

    @ApiModelProperty("用户登录拓展信息")
    private UserExtensionInfoDto userExtensionInfo;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public UserExtensionInfoDto getUserExtensionInfo() {
        return userExtensionInfo;
    }

    public void setUserExtensionInfo(UserExtensionInfoDto userExtensionInfo) {
        this.userExtensionInfo = userExtensionInfo;
    }
}
