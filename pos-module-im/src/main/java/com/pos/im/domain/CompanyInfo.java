/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * IM公司信息的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/11/23
 */
public class CompanyInfo implements Serializable {

    private Long id;

    private Long companyId; // 公司ID

    private String name; // 公司名称

    private Long areaId; // 所属地区ID（叶子节点）

    private String address; // 公司地址

    private String logoImage; // 公司LOGO

    private Date createTime; // 公司信息初始化到IM的时间

    private Date updateTime; // 公司信息最近一次同步到IM的时间

    private Boolean available; // 是否可用

    private Boolean imSmsEnable; // IM短信开关是否开启

    private Boolean imNoticeEnable; // IM通知开关是否开启

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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