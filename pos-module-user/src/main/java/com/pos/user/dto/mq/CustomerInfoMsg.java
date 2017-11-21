/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.mq;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 客户信息的消息对象，用于推客客户挂你绑定
 *
 * @author wangbing
 * @version 1.0, 2017/04/10
 */
public class CustomerInfoMsg implements Serializable {

    private static final long serialVersionUID = -5092713757468816215L;

    private Long userId; // 注册客户id

    private String userPhone; // 注册客户手机号码

    private String invitationCode; // 推客邀请码

    private String userName; // 注册客户姓名，例：业主****，注：在填写了邀请码时此字段才有意义

    public CustomerInfoMsg() {
    }

    public CustomerInfoMsg(Long userId, String userPhone, String invitationCode) {
        this.userId = userId;
        this.userPhone = userPhone;
        this.invitationCode = invitationCode;
        if (!StringUtils.isEmpty(invitationCode)) {
            this.userName = "业主" + userPhone.substring(userPhone.length() - 4, userPhone.length());
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
