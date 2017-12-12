/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.feedback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户反馈意见的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/10/8
 */
@ApiModel
public class UserFeedbackDto implements Serializable {

    private static final long serialVersionUID = -5627866382872422016L;
    @ApiModelProperty("反馈意见编号")
    private Long id;

    @ApiModelProperty("用户ID（可空）")
    private Long userId;

    @ApiModelProperty("用户手机号（可空）")
    private String userPhone;

    @ApiModelProperty("设备唯一ID")
    private String deviceId;

    @ApiModelProperty("设备操作系统（含版本号）")
    private String deviceOS;

    @ApiModelProperty("设备型号（即机型）")
    private String deviceModel;

    @ApiModelProperty("APP类型：c = 客户端app，e = 业者端app")
    private String appType;

    @ApiModelProperty("APP版本号")
    private String appVersion;

    @ApiModelProperty("吐槽内容")
    private String content;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonIgnore
    private String imagesStr;

    @ApiModelProperty("吐槽图片的URL列表（可空）")
    public List<String> getImages() {
        return !Strings.isNullOrEmpty(imagesStr)
                ? Lists.newArrayList(Splitter.on(",").split(imagesStr)) : null;
    }

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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getImagesStr() {
        return imagesStr;
    }

    public void setImagesStr(String imagesStr) {
        this.imagesStr = imagesStr;
    }

}