/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import com.pos.user.dto.employee.EmployeeServiceContentDto;

import java.io.Serializable;

/**
 * B端从业者的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public class Employee implements Serializable {

    private static final long serialVersionUID = 2385245030931058224L;

    private Long id;

    private Long userId; // User.id

    private byte userDetailType; // 用户细分类型

    private Long companyId; // 所属公司ID

    private String nickName; // 昵称

    private String headImage; // 头像

    private String lifePhotos; // 个人形象照

    private String resume; // 个人简介

    private boolean quitJobs; // 是否离职

    private boolean publicPhone; // 是否公开电话号码

    /**
     * @see EmployeeServiceContentDto
     */
    private String serviceContent;

    private String advertorial; // 软文

    private String invitationCode;//主材补贴的邀请码

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

    public byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(byte userDetailType) {
        this.userDetailType = userDetailType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getLifePhotos() {
        return lifePhotos;
    }

    public void setLifePhotos(String lifePhotos) {
        this.lifePhotos = lifePhotos;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public boolean isQuitJobs() {
        return quitJobs;
    }

    public void setQuitJobs(boolean quitJobs) {
        this.quitJobs = quitJobs;
    }

    public boolean isPublicPhone() {
        return publicPhone;
    }

    public void setPublicPhone(boolean publicPhone) {
        this.publicPhone = publicPhone;
    }

    public String getAdvertorial() {
        return advertorial;
    }

    public void setAdvertorial(String advertorial) {
        this.advertorial = advertorial;
    }

    public String getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }
}