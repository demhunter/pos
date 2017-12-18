/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import java.io.Serializable;

/**
 * APP版本配置的领域对象.
 *
 * @author wangbing
 * @version 2.0, 2016/12/13
 */
public class AppVersionConfig implements Serializable {

    private static final long serialVersionUID = 1202364801612992791L;

    private Long id;

    private String currentVersion; // 当前生产环境运行的版本号

    private String minVersion; // 支持的最小版本号，低于该版本号必须升级APP

    private String currentUrl; // 当前生产环境的访问地址

    private String latestUrl; // 最新版本的访问地址（一般情况下，该值不为空，表示有新版本处于审核/测试中）

    private String latestAndroidUrl; // 最新版本的官方下载地址（安卓）

    private String latestAndroidMd5; // 最新版本的官方下载文件的MD5（安卓）

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public String getLatestUrl() {
        return latestUrl;
    }

    public void setLatestUrl(String latestUrl) {
        this.latestUrl = latestUrl;
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