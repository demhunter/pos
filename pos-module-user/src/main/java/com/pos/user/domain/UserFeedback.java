/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户反馈领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/10/8
 */
public class UserFeedback implements Serializable {

    private static final long serialVersionUID = -4988100718972729640L;

    private Long id;

    private Long userId; // 用户ID，-1表示为空

    private String deviceId; // 设备唯一ID

    private String deviceOS; // 设备操作系统（含版本号）

    private String deviceModel; // 设备型号（即机型）

    private String appType; // APP类型：c = 客户端app，e = 业者端app

    private String appVersion; // APP版本号

    private String content; // 吐槽内容

    private String images; // 吐槽图片的URL，最多4张以逗号分隔

    private Date createTime; // 创建时间

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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}