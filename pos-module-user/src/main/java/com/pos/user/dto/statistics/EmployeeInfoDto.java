/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.user.constant.EmployeeType;

import java.io.Serializable;
import java.util.Date;

/**
 * 平台数据统计业者信息
 *
 * @author wangbing
 * @version 1.0, 2017/03/31
 */
@Deprecated
public class EmployeeInfoDto implements Serializable {

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("电话号码")
    private String phone;

    @ApiModelProperty("业者角色code（int）")
    private Byte employeeType;

    @ApiModelProperty("业者角色描述")
    public String getUserDetailTypeDesc() {
        return employeeType == null ? null : parseEmployeeType().getDesc();
    }

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("关联作品数")
    private Integer caseCount;

    @ApiModelProperty("最后一次登录时间")
    private Date lastLoginTime;

    public EmployeeType parseEmployeeType() {
        return EmployeeType.getEnum(employeeType);
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Byte getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Byte employeeType) {
        this.employeeType = employeeType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(Integer caseCount) {
        this.caseCount = caseCount;
    }
}
