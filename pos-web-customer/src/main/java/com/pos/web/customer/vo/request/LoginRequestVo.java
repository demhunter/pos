/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.vo.request;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 登陆信息Vo
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class LoginRequestVo implements Serializable {

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("密码")
    private String password;

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
}
