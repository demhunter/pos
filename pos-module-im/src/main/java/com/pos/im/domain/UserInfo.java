/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import com.pos.im.dto.user.UserInfoDto;
import com.pos.im.service.IMUserService;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * IM用户信息的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/8/8
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 7952615587477229873L;

    private Long id;

    private Long userId; // 用户ID

    private String userType; // 用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员

    private Byte userDetailType; // 用户细分类型，取值根据userType而不同，具体参考User Module

    private String userTitle; // 用户头衔(目前即用户细分类型的中文描述)

    private String showName; // 用户在IM中显示的名称

    private String showHead; // 用户在IM中显示的头像

    private Long companyId; // B端/业者所属的公司ID

    private byte gender; // 性别：1 = 男，2 = 女，0 = 保密

    private String phone; // 联系电话

    private boolean publicPhone; // 是否公开电话号码

    private String appendGreeting; // 用户追加的问候语

    private Date createTime; // 用户信息初始化到IM的时间

    private Date updateTime; // 用户信息最近一次同步到IM的时间

    private boolean available; // 是否可用

    private String imUid; // 与IM的唯一通信id

    public boolean isCustomer() {
        return IMUserService.USER_TYPE_CUSTOMER.equals(userType);
    }

    public boolean isBusiness() {
        return IMUserService.USER_TYPE_BUSINESS.equals(userType);
    }

    public boolean isEmployee() {
        return IMUserService.USER_TYPE_EMPLOYEE.equals(userType);
    }

    public boolean isManager() {
        return IMUserService.USER_TYPE_MANAGER.equals(userType);
    }

    public String getImUid() {
        return imUid;
    }

    public void setImUid(String imUid) {
        this.imUid = imUid;
    }

    public String getFullName() {
        if (isCustomer()) {
            return "(业主)" + showName;
        } else if (isManager()) {
            return "(鹦鹉美家客服)" + showName;
        } else if (isEmployee()) {
            if (userDetailType != null && IMUserService.PLATFORM_EMPLOYEE_TYPE == userDetailType) {
                return "(家居顾问)" + showName;
            } else {
                return "(业者)" + showName;
            }
        } else {
            return showName;
        }
    }

    public static UserInfo convertBy(UserInfoDto dto) {
        UserInfo entity = new UserInfo();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPublicPhone() {
        return publicPhone;
    }

    public void setPublicPhone(boolean publicPhone) {
        this.publicPhone = publicPhone;
    }

    public String getAppendGreeting() {
        return appendGreeting;
    }

    public void setAppendGreeting(String appendGreeting) {
        this.appendGreeting = appendGreeting;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}