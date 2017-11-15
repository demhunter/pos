/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.vo.request;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 注册请求Vo
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class RegisterRequestVo implements Serializable {

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("短信验证码")
    private String smsCode;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("v1.0.0 * 登录的类型（1 = 推广发展客户的链接，2 = 推广发展推客的链接）")
    private Integer type;

    @ApiModelProperty("v1.0.0 * 上一级的userId")
    private Long leaderId;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }
}
