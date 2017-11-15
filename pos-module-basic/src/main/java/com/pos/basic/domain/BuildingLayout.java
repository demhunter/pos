/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 楼盘户型的领域对象.
 *
 * @author wayne
 * @version 1.0, 2017/3/15
 */
public class BuildingLayout implements Serializable {

    private Long id;

    private String name; // 户型名称

    private Long buildingId; // 所属楼盘ID

    private Byte bedroom; // 户型-室

    private Byte livingRoom; // 户型-厅

    private Byte toilet; // 户型-卫

    private BigDecimal houseArea; // 户型面积

    private String layoutImage; // 户型图

    private Boolean available; // 是否可用

    private Date createTime;

    private Date updateTime;

    private Long createUserId;

    private Long updateUserId;

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

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Byte getBedroom() {
        return bedroom;
    }

    public void setBedroom(Byte bedroom) {
        this.bedroom = bedroom;
    }

    public Byte getLivingRoom() {
        return livingRoom;
    }

    public void setLivingRoom(Byte livingRoom) {
        this.livingRoom = livingRoom;
    }

    public Byte getToilet() {
        return toilet;
    }

    public void setToilet(Byte toilet) {
        this.toilet = toilet;
    }

    public BigDecimal getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(BigDecimal houseArea) {
        this.houseArea = houseArea;
    }

    public String getLayoutImage() {
        return layoutImage;
    }

    public void setLayoutImage(String layoutImage) {
        this.layoutImage = layoutImage;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
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