/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.session;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.user.dto.UserDto;
import com.pos.user.dto.manager.ManagerDto;

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

    private String userPhone; // 登录手机号，如果注册时使用的是账号名，则默认为账号名

    private String userType; // 用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员

    private Byte userDetailType; // 用户细分类型（可空）

    private Long companyId; // 所属公司ID（可空）

    private String showName; // 用户显示给其他人看到的名称

    private String showHead; // 用户显示给其他人看到的头像

    private String imUid; // 用户与第三方IM平台的唯一通信ID

    public UserInfo() {
    }

    public UserInfo(UserDto userDto) {
        this.id = userDto.getId();
        this.userName = userDto.getUserName();
        this.userPhone = userDto.getUserPhone();
        this.userType = userDto.getUserType();
        this.showName = userDto.getShowName();
        this.showHead = userDto.getShowHead();

        if (userDto instanceof ManagerDto) {
            ManagerDto managerDto = (ManagerDto) userDto;
            this.userDetailType = managerDto.getUserDetailType();
        }

        this.imUid = userDto.getImUid();
    }

    public UserIdentifier buildUserIdentifier() {
        return new UserIdentifier(id, userType);
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    public String getImUid() {
        return imUid;
    }

    public void setImUid(String imUid) {
        this.imUid = imUid;
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

    public Byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(Byte userDetailType) {
        this.userDetailType = userDetailType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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