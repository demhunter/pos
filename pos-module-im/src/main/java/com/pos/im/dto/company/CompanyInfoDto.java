/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.company;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * IM公司信息DTO.
 *
 * @author wayne
 * @version 1.0, 2016/11/23
 */
@ApiModel
public class CompanyInfoDto implements Serializable {

    private static final long serialVersionUID = -9160079201136595162L;
    
    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("公司名称")
    private String name;

    @ApiModelProperty("所属地区ID（叶子节点）")
    private Long areaId;

    @ApiModelProperty("公司地址")
    private String address;

    @ApiModelProperty("公司LOGO")
    private String logoImage;

    @ApiModelProperty("公司信息初始化到IM的时间")
    private Date createTime;

    @ApiModelProperty("公司信息最近一次同步到IM的时间")
    private Date updateTime;

    @ApiModelProperty("是否可用")
    private Boolean available;

    @ApiModelProperty("IM短信开关是否开启")
    private Boolean imSmsEnable;

    @ApiModelProperty("IM通知开关是否开启")
    private Boolean imNoticeEnable;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getImSmsEnable() {
        return imSmsEnable;
    }

    public void setImSmsEnable(Boolean imSmsEnable) {
        this.imSmsEnable = imSmsEnable;
    }

    public Boolean getImNoticeEnable() {
        return imNoticeEnable;
    }

    public void setImNoticeEnable(Boolean imNoticeEnable) {
        this.imNoticeEnable = imNoticeEnable;
    }

}