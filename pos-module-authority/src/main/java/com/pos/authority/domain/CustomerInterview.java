/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户回访信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class CustomerInterview implements Serializable {

    private static final long serialVersionUID = 3333443999113490786L;
    private Long id; // 自增主键id

    private Long userId; // 回访用户id

    private String content; // 回访内容记录

    private Long createUserId; // 回访记录创建人

    private Date createTime; // 回访时间

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
