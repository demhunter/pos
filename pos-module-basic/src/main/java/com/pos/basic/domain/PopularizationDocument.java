/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 推广文案信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class PopularizationDocument implements Serializable {

    private static final long serialVersionUID = 2440364287870340460L;
    private Long id; // 自增主键id

    private String document;  // 推广文本

    private String images; // 推广图片列表

    private Boolean available; // 是否可用

    private Long updateUserId; // 更新操作人id

    private Date updateTime; // 更新操作时间

    private Long createUserId; // 创建人id

    private Date createTime; // 创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
