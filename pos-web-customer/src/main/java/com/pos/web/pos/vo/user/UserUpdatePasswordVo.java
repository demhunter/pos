/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.user;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 密码更换请求VO
 *
 * @author wangbing
 * @version 1.0, 2017/12/6
 */
public class UserUpdatePasswordVo implements Serializable {

    @ApiModelProperty("旧密码")
    private String oldPassword;

    @ApiModelProperty("新密码密码")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
