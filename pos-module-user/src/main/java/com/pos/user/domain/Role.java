/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import java.io.Serializable;

/**
 * 角色配置领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/6/2
 */
public class Role implements Serializable {

    private static final long serialVersionUID = 4845766478330983827L;

    private Long id;

    private String name;

    private Long parentId;

    private String module; // 角色所属的模块，以系统模块名定义

    private byte level; // 角色层级

    private String resourceIds; // 角色拥有的资源ID，以逗号分隔，如1,2,3... 通配符*表示拥有所有资源权限(root)

    private byte orderNum; // 显示排序（正序）

    private boolean available; // 是否可用

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
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