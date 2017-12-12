/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 版本更新说明信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class VersionInstruction implements Serializable {

    private static final long serialVersionUID = -2784483707899709897L;

    private Long id; // 自增主键id

    private String version; // 版本号，如xx.xx.xx

    private String instruction; // 版本介绍

    private Boolean available; // 是否启用

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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
