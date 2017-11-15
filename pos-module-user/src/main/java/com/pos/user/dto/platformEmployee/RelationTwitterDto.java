/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/7/10
 */
public class RelationTwitterDto implements Serializable {

    private static final long serialVersionUID = -1128680752522553892L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String mobilePhone;

    @ApiModelProperty("是否启用")
    private boolean enable;

    @ApiModelProperty(" date 关联时间")
    private Date relationDate;

    @ApiModelProperty("userId")
    private Long userId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Date getRelationDate() {
        return relationDate;
    }

    public void setRelationDate(Date relationDate) {
        this.relationDate = relationDate;
    }
}
