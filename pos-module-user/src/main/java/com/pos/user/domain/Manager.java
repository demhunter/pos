/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import java.io.Serializable;

/**
 * 平台管理员的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public class Manager implements Serializable {

    private static final long serialVersionUID = 2343199817627835020L;

    private Long id;

    private Long userId; // User.id

    private byte userDetailType; // 用户细分类型

    private String headImage; // 头像

    private boolean quitJobs; // 是否离职

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

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public boolean isQuitJobs() {
        return quitJobs;
    }

    public void setQuitJobs(boolean quitJobs) {
        this.quitJobs = quitJobs;
    }

}