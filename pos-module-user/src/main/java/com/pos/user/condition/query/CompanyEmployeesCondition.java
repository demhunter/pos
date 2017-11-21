/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

import com.pos.user.constant.EmployeeType;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 公司业者查询条件
 *
 * @author cc
 * @version 1.0, 16/8/3
 */
public class CompanyEmployeesCondition implements Serializable {

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 查询关键字
     */
    private String queryKey;

    /**
     * 业者角色 {@link EmployeeType #value}
     */
    private Byte userDetailType;

    @ApiModelProperty("是否离职")
    private boolean quitJobs;

    /**
     * 是否可用
     */
    private Integer available;

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public Byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(Byte userDetailType) {
        this.userDetailType = userDetailType;
    }

    public boolean isQuitJobs() {
        return quitJobs;
    }

    public void setQuitJobs(boolean quitJobs) {
        this.quitJobs = quitJobs;
    }
}
