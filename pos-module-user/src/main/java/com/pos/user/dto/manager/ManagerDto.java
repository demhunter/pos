/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.manager;

import com.pos.user.dto.UserDto;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理信息DTO
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public class ManagerDto extends UserDto implements Serializable {

    @ApiModelProperty("管理员类型")
    private Integer managerType;

    @ApiModelProperty("管理员姓名")
    private String name;

    @ApiModelProperty("管理员头像")
    private String headImage;

    @ApiModelProperty("是否离职")
    private Boolean dimission;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @Override
    public String getShowName() {
        return name;
    }

    @Override
    public String getShowHead() {
        return StringUtils.isEmpty(headImage) ? DEFAULT_HEAD_IMAGE : headImage;
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
