/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 睿智
 * @version 1.0, 2017/9/13
 */
public class GetSignDto implements Serializable {

    private static final long serialVersionUID = 1707157446261388045L;

    @ApiModelProperty("appId")
    private String appId;

    @ApiModelProperty("时间戳")
    private String timestamp;

    @ApiModelProperty("随机字符串")
    private String nonceStr;

    @ApiModelProperty("签名")
    private String signature;

    @ApiModelProperty("日期")
    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
