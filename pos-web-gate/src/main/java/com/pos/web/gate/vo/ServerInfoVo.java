/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.gate.vo;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 服务器信息VO
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
public class ServerInfoVo implements Serializable {

    private static final long serialVersionUID = 333097626667588657L;
    @ApiModelProperty("当前版本号")
    private String currentVersion;

    @ApiModelProperty("服务器的访问地址，即app访问的baseUrl")
    private String serverUrl;

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
