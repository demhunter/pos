/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

import java.io.Serializable;

/**
 * 平台管理员列表查询条件.
 *
 * @author wayne
 * @version 1.0, 2016/8/31
 */
public class ManagerListCondition implements Serializable {

    private Byte userDetailType; // 用户细分类型

    private String name; // 按员工姓名进行模糊搜索

    private Boolean quitJobs; // true查询离职员工, false查询在职员工, null查询所有员工

    private Boolean available; // true查询已启用的账号, false查询被禁用的账号, null查询所有账号

    public Byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(Byte userDetailType) {
        this.userDetailType = userDetailType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getQuitJobs() {
        return quitJobs;
    }

    public void setQuitJobs(Boolean quitJobs) {
        this.quitJobs = quitJobs;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

}