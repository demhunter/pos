/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.version;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 版本更新信息Vo
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
public class VersionVo implements Serializable {

    private static final long serialVersionUID = -7637540052004818914L;
    @ApiModelProperty("当前版本（xx.xx.xx）")
    private String currentVersion;

    @ApiModelProperty("最新版本（xx.xx.xx）")
    private String latestVersion;

    @ApiModelProperty("true：需要更新；false：不要更新")
    private Boolean needUpdate;

    @ApiModelProperty("true：强制更新；false：兼容更新（当needUpdate = true时，此字段有效）")
    private Boolean forceUpdate;

    @ApiModelProperty("版本更新内容（当needUpdate = true时，此字段有效）")
    private String updateInstruction;

    @ApiModelProperty("最新版本Android下载地址（当needUpdate = true时，此字段有效）")
    private String latestAndroidUrl;

    @ApiModelProperty("最新版本Android的MD5值（当needUpdate = true时，此字段有效）")
    private String latestAndroidMD5;

    public String getLatestAndroidUrl() {
        return latestAndroidUrl;
    }

    public void setLatestAndroidUrl(String latestAndroidUrl) {
        this.latestAndroidUrl = latestAndroidUrl;
    }

    public String getLatestAndroidMD5() {
        return latestAndroidMD5;
    }

    public void setLatestAndroidMD5(String latestAndroidMD5) {
        this.latestAndroidMD5 = latestAndroidMD5;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public Boolean getNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(Boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUpdateInstruction() {
        return updateInstruction;
    }

    public void setUpdateInstruction(String updateInstruction) {
        this.updateInstruction = updateInstruction;
    }
}
