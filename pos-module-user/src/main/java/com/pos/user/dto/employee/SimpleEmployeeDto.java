/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.employee;

import com.pos.user.constant.EmployeeType;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * 雇员简要信息Dto
 *
 * @author cc
 * @version 1.0, 16/9/5
 */
public class SimpleEmployeeDto implements Serializable {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String headImage;

    /**
     * 雇员姓名
     */
    @ApiModelProperty(value = "雇员姓名")
    private String name;

    /**
     * 雇员角色 {@link EmployeeType#desc}
     */
    @ApiModelProperty(value = "雇员角色")
    private String title;

    public SimpleEmployeeDto() {
    }

    public SimpleEmployeeDto(EmployeeDto employeeDto) {
        userId = employeeDto.getId();
        headImage = employeeDto.getHeadImage();
        name = employeeDto.getName();
        title = employeeDto.getUserDetailTypeDesc();
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
