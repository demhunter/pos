/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.session;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.user.dto.UserDto;

import java.io.Serializable;

/**
 * 用户会话信息.
 *
 * @author wayne
 * @version 1.0, 2016/8/5
 */
public final class UserInfo implements Serializable {

    private static final long serialVersionUID = 8498337078317196586L;

    private Long id; // 用户编号

    private String userName; // 登录账号名，如果注册时使用的是手机号，则默认为手机号

    private String userPhone; // 登录手机号，如果注册时使用的是账号名，则默认为空

    private String userType; // 用户类型：c = C端用户，m = 后台管理员

    private String showName; // 用户显示给其他人看到的名称

    private String showHead; // 用户显示给其他人看到的头像

    public UserInfo() {
    }

    public UserInfo(UserDto userDto) {
        this.id = userDto.getId();
        this.userName = userDto.getLoginName();
        this.userType = userDto.getUserType();
        this.showName = userDto.getShowName();
        this.showHead = userDto.getShowHead();
        this.userPhone = userDto.getPhone();
    }

    public UserIdentifier buildUserIdentifier() {
        return new UserIdentifier(id, userType);
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowHead() {
        return showHead;
    }

    public void setShowHead(String showHead) {
        this.showHead = showHead;
    }

}