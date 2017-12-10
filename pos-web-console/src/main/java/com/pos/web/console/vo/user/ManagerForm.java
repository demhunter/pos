/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.vo.user;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 平台管理员新增/修改的FormBean.
 *
 * @author wayne
 * @version 1.0, 2016/8/31
 */
@ApiModel
public class ManagerForm {

    @ApiModelProperty("账号名")
    private String userName;

    @ApiModelProperty("手机号")
    private String userPhone;

    @ApiModelProperty("真实姓名")
    private String name;

    @ApiModelProperty("用户细分类型：1 = 普通，2 = 客服，3 = 商务拓展")
    private byte userDetailType;

    @ApiModelProperty("头像")
    private String headImage;

    @ApiModelProperty("是否离职")
    private boolean quitJobs;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(byte userDetailType) {
        this.userDetailType = userDetailType;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public boolean isQuitJobs() {
        return quitJobs;
    }

    public void setQuitJobs(boolean quitJobs) {
        this.quitJobs = quitJobs;
    }

}