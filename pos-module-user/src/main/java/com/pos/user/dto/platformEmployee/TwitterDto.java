/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/7/10
 */
public class TwitterDto implements Serializable {

    private static final long serialVersionUID = -1550583779423027197L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("推客姓名")
    private String name;

    @ApiModelProperty("推客电话号码")
    private String mobilePhone;

    @ApiModelProperty("家居顾问的名字")
    private String peName;

    @ApiModelProperty("家居顾问的服务区域")
    private String peArea;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPeName() {
        return peName;
    }

    public void setPeName(String peName) {
        this.peName = peName;
    }

    public String getPeArea() {
        return peArea;
    }

    public void setPeArea(String peArea) {
        this.peArea = peArea;
    }
}
