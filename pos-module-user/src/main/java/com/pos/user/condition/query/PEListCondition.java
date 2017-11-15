/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/7/11
 */
public class PEListCondition implements Serializable {

    private Long areaId;

    private Integer manager;

    private Integer distribution;

    private Integer enable;

    private String name;

    private String mobilePhone;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public Integer getDistribution() {
        return distribution;
    }

    public void setDistribution(Integer distribution) {
        this.distribution = distribution;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
