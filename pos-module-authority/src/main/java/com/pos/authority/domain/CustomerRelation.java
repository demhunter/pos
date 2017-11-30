/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户上下级关联信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class CustomerRelation implements Serializable {

    private Long id; // 自增主键id

    private Long userId; // 子用户id

    private Long parentUserId; // 父用户id

    private String remark; // 父用户对子用户的备注

    private Long updateUserId; // 更新操作人id

    private Date updateTime; // 更新操作时间

    private Date createTime; // 父子关系关联时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
