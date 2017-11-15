/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import com.pos.basic.constant.DecorationStandard;

import java.io.Serializable;
import java.util.Date;

/**
 * 楼盘领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/6/3
 */
public class Building implements Serializable {

    private Long id;

    private String name;

    private Long areaId; // 地区ID（叶子节点）

    private String address; // 楼盘地址

    private String developer; // 开发商

    /** {@link DecorationStandard} */
    private Byte decoration; // 装修标准：1 = 毛坯，2 = 精装

    private boolean available; // 是否可用

    private Date createTime; // 创建时间

    private Date updateTime; // 更新时间

    private Long createUserId; // 创建人UID

    private Long updateUserId; // 更新人UID

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public Byte getDecoration() {
        return decoration;
    }

    public void setDecoration(Byte decoration) {
        this.decoration = decoration;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

}