/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import java.io.Serializable;

/**
 * B端用户的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public class Merchant implements Serializable {

    private static final long serialVersionUID = 2045134078437702711L;

    private Long id;

    private Long userId; // User.id

    private byte userDetailType; // 用户细分类型

    private Long companyId; // 所属公司ID

    private String headImage; // 头像

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

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

}