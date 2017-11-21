/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.feedback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;
import java.util.List;

/**
 * 新建反馈意见的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/10/8
 */
@ApiModel
public class NewFeedbackDto implements Serializable {

    @ApiModelProperty("设备唯一ID")
    private String deviceId;

    @ApiModelProperty("设备操作系统（含版本号）")
    private String deviceOS;

    @ApiModelProperty("设备型号（即机型）")
    private String deviceModel;

    @ApiModelProperty("APP版本号")
    private String appVersion;

    @ApiModelProperty("吐槽内容")
    private String content;

    @ApiModelProperty("吐槽图片的URL列表")
    private List<String> images;

    /**
     * APP类型：c = 客户端app，e = 业者端app
     */
    @JsonIgnore
    private String appType;

    /**
     * 检查字段完整性.
     *
     * @param fieldPrefix 抛出异常时提示错误字段的前缀名, 可以为空
     * @throws ValidationException 未设置不能为空的字段, 或者字段值不符合约定
     */
    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(deviceId, fieldPrefix + "deviceId");
        FieldChecker.checkEmpty(deviceOS, fieldPrefix + "deviceOS");
        FieldChecker.checkEmpty(deviceModel, fieldPrefix + "deviceModel");
        FieldChecker.checkEmpty(appType, fieldPrefix + "appType");
        FieldChecker.checkEmpty(appVersion, fieldPrefix + "appVersion");
        FieldChecker.checkMinMaxLength(content, 1, 200, "content");
    }

    public String getAppType() {
        return appType;
    }

    public NewFeedbackDto setAppType(String appType) {
        this.appType = appType;
        return this;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}