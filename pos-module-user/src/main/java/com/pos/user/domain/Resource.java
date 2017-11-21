/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import java.io.Serializable;

/**
 * 资源配置领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/6/2
 */
public class Resource implements Serializable {

    private static final long serialVersionUID = -6316392765115578777L;

    private Long id;

    private String name;

    private Long parentId;

    private String module; // 资源所属的模块，以系统模块名定义

    private byte level; // 资源层级

    private String url; // 资源URL的相对路径，如果只负责展开子节点则为空

    private String method; // 资源URL的Method属性，如果只负责展开子节点则为空

    private byte orderNum; // 显示排序（正序）

    private boolean menu; // 资源是否是个菜单;

    private String menuIcon; // 菜单ICON样式

    private String menuHref; // 菜单链接，默认是url，可以是#号，表示展开子菜单

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public byte getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(byte orderNum) {
        this.orderNum = orderNum;
    }

    public boolean isMenu() {
        return menu;
    }

    public void setMenu(boolean menu) {
        this.menu = menu;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuHref() {
        return menuHref;
    }

    public void setMenuHref(String menuHref) {
        this.menuHref = menuHref;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}