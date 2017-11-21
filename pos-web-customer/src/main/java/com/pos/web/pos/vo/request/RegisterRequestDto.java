/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.request;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class RegisterRequestDto implements Serializable {

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("短信验证码")
    private String smsCode;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("选填，登录的类型（int，1 = 推广发展客户的链接，2 = 推广发展渠推客的链接）")
    private Byte type;

    @ApiModelProperty("选填，上一级的userId")
    private Long leaderId;

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

}
