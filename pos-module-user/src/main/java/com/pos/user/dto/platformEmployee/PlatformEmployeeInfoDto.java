/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author lifei
 * @version 1.0, 2017/9/5
 */
public class PlatformEmployeeInfoDto implements Serializable {

    @ApiModelProperty("家居顾问姓名")
    private String peName;

    @ApiModelProperty("家居顾问电话")
    private String phone;

    @ApiModelProperty("所在区域ID")
    private Long areaId;

    @ApiModelProperty("所在城市")
    private String cityName;

    public String getPeName() {
        return peName;
    }

    public void setPeName(String peName) {
        this.peName = peName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
