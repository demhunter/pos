/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.gate.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * APP版本配置的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/8/16
 */
public class AppVersionDto implements Serializable {

    private static final long serialVersionUID = -2052642969341733705L;

    @ApiModelProperty("配置编号")
    private Long id;

    @ApiModelProperty("APP类型：c = 客户端app，e = 业者端app")
    private String appType;

    @ApiModelProperty("当前生产环境运行的版本号")
    private String curVersion;

    @ApiModelProperty("支持的最小版本号，低于该版本号必须升级APP")
    private String minVersion;

    @ApiModelProperty("当前生产环境的访问地址")
    private String curUrl;

    @ApiModelProperty("最新版本的访问地址（一般情况下，该值不为空，表示有新版本处于审核/测试中）")
    private String latestUrl;

    @ApiModelProperty("当前生产环境连接第三方IM平台的Key")
    private String curImKey;

    @ApiModelProperty("最新版本连接第三方IM平台的Key（值说明同latestUrl）")
    private String latestImKey;

    @ApiModelProperty("最新版本的官方下载地址（安卓）")
    private String latestAndroidUrl;

    @ApiModelProperty("最新版本的官方下载文件的MD5（安卓）")
    private String latestAndroidMd5;

    @ApiModelProperty("当前生产环境的WEB访问地址,H5使用")
    private String webUrl;

    @ApiModelProperty("当前预发布环境的WEB访问地址，H5使用，测试用")
    private String prewebUrl;

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    public String getPrewebUrl() {
        return prewebUrl;
    }

    public void setPrewebUrl(String prewebUrl) {
        this.prewebUrl = prewebUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getCurVersion() {
        return curVersion;
    }

    public void setCurVersion(String curVersion) {
        this.curVersion = curVersion;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getCurUrl() {
        return curUrl;
    }

    public void setCurUrl(String curUrl) {
        this.curUrl = curUrl;
    }

    public String getLatestUrl() {
        return latestUrl;
    }

    public void setLatestUrl(String latestUrl) {
        this.latestUrl = latestUrl;
    }

    public String getCurImKey() {
        return curImKey;
    }

    public void setCurImKey(String curImKey) {
        this.curImKey = curImKey;
    }

    public String getLatestImKey() {
        return latestImKey;
    }

    public void setLatestImKey(String latestImKey) {
        this.latestImKey = latestImKey;
    }

    public String getLatestAndroidUrl() {
        return latestAndroidUrl;
    }

    public void setLatestAndroidUrl(String latestAndroidUrl) {
        this.latestAndroidUrl = latestAndroidUrl;
    }

    public String getLatestAndroidMd5() {
        return latestAndroidMd5;
    }

    public void setLatestAndroidMd5(String latestAndroidMd5) {
        this.latestAndroidMd5 = latestAndroidMd5;
    }

}