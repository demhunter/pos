/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

import com.pos.user.constant.PlatformEmployeeType;

import java.io.Serializable;

/**
 * 平台家居顾问查询条件定义
 *
 * @author wangbing
 * @version 1.0, 2017/7/6
 */
public class PlatformEmployeeCondition implements Serializable {

    private Long id; // 指定的家居顾问id

    private Long userId; // 指定家居顾问的用户id

    private Long areaId; // 服务区域

    private String workNo; // 指定编号（工号）

    /**
     * @see PlatformEmployeeType
     */
    private Byte userDetailType; // 指定用户细分类型（1：客户经理，2：普通家居顾问）

    private Boolean distribution; // 是否接受分配

    private Boolean quit; // 是否离职

    private Boolean enable; // 是否启用

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

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public Byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(Byte userDetailType) {
        this.userDetailType = userDetailType;
    }

    public Boolean getDistribution() {
        return distribution;
    }

    public void setDistribution(Boolean distribution) {
        this.distribution = distribution;
    }

    public Boolean getQuit() {
        return quit;
    }

    public void setQuit(Boolean quit) {
        this.quit = quit;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
