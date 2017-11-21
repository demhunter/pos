/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import com.pos.common.util.basic.PrintableBeanUtils;

import java.util.Date;

/**
 * 热词领域对象
 *
 * @author wangbing
 * @version 1.0, 2016/12/29
 */
public class HotKey {

    private Long id; // 主键ID

    private String keyName; // 热词名称

    private Byte keyType; // 热词类型（1 = 作品）

    private Long totalCount; // 热词被搜索的总次数

    private Byte status; // 热词状态（0 = 不可用， 1 = 可用）

    private Date createTime; // 热词创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Byte getKeyType() {
        return keyType;
    }

    public void setKeyType(Byte keyType) {
        this.keyType = keyType;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
