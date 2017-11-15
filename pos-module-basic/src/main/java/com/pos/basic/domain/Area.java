/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * 地区领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/6/2
 */
public class Area implements Serializable {

    private static final long serialVersionUID = 3878391750688306431L;

    private Long id;

    private String name;

    private String shortName;

    private Long parentId;

    private byte level; // 地区层级，目前设计最多为3级(直辖市只有2级)，1级为省，2级为市，3级为县/区

    private byte orderNum; // 显示排序（正序）

    private boolean available; // 是否可用

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public byte getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(byte orderNum) {
        this.orderNum = orderNum;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}