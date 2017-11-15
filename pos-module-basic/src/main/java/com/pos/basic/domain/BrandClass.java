/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌关联的分类领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/9/28
 */
public class BrandClass implements Serializable {

    private static final long serialVersionUID = 4237774122788393004L;

    private Long id;

    private Long brandId; // 品牌ID

    private byte type; // 品牌大类

    private short subType; // 品牌子类

    private Date createTime; // 创建时间

    private Long createUserId; // 创建人UID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public BrandClass setBrandId(Long brandId) {
        this.brandId = brandId;
        return this;
    }

    public byte getType() {
        return type;
    }

    public BrandClass setType(byte type) {
        this.type = type;
        return this;
    }

    public short getSubType() {
        return subType;
    }

    public BrandClass setSubType(short subType) {
        this.subType = subType;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public BrandClass setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public BrandClass setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
        return this;
    }

}