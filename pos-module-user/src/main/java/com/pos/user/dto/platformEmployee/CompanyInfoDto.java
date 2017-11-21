/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 平台业者查询公司信息
 *
 * @author wangbing
 * @version 1.0, 2017/7/12
 */
public class CompanyInfoDto implements Serializable {

    @ApiModelProperty("公司编号")
    private Long id;

    @ApiModelProperty("公司名称")
    private String name;

    @ApiModelProperty("地区最后一级ID")
    private Long areaId;

    @ApiModelProperty("省级id")
    private Long provinceId;

    @ApiModelProperty("城市id")
    private Long cityId;

    @ApiModelProperty("是否可用")
    private boolean available;

    @ApiModelProperty("是否删除")
    private boolean deleted;

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

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
