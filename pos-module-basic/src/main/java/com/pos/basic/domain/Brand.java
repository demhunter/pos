/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/8/29
 */
public class Brand implements Serializable {

    private static final long serialVersionUID = -6747097103072820516L;

    private Long id;

    private String name;

    private String logo;

    private String website; // 品牌官网

    private boolean available;

    private Date createTime;

    private Date updateTime;

    private Long createUserId;

    private Long updateUserId;

    public Long getId() {
        return id;
    }

    public Brand setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Brand setName(String name) {
        this.name = name;
        return this;
    }

    public String getLogo() {
        return logo;
    }

    public Brand setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public boolean isAvailable() {
        return available;
    }

    public Brand setAvailable(boolean available) {
        this.available = available;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Brand setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Brand setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public Brand setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
        return this;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public Brand setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public Brand setWebsite(String website) {
        this.website = website;
        return this;
    }
}