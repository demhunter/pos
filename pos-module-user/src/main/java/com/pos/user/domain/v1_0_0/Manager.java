/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain.v1_0_0;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员信息领域对象
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class Manager implements Serializable {

    private Long id;

    private Long userId; // 用户id

    private Integer managerType; // 管理员类型，往后拓展权限时，可以以此类型区分权限

    private String name; // 管理员姓名

    private String headImage; // 管理员头像

    private Boolean dimission; // 是否离职

    private Date updateTime; // 更新时间

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

    public Integer getManagerType() {
        return managerType;
    }

    public void setManagerType(Integer managerType) {
        this.managerType = managerType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Boolean getDimission() {
        return dimission;
    }

    public void setDimission(Boolean dimission) {
        this.dimission = dimission;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
