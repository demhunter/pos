/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/10/24
 */
public class SessionUserDto implements Serializable {

    private static final long serialVersionUID = 8962753318208342762L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("用户手机号")
    private String mobilePhone;

    @ApiModelProperty("用户属性 比如c e m")
    private String userType;

    @ApiModelProperty("用户详细类型 比如设计师 商务代表")
    private String userDetailType;

    @ApiModelProperty("公司名")
    private String company;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(String userDetailType) {
        this.userDetailType = userDetailType;
    }
}
