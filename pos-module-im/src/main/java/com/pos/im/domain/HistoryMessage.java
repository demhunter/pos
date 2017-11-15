/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import com.pos.im.constant.HistoryMessageStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * IM历史消息的处理记录的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/10/20
 */
public class HistoryMessage implements Serializable {

    private static final long serialVersionUID = -3432936608819141446L;

    private Long id;

    private String histUrl; // 历史消息的下载地址

    private int histDate; // 历史消息的产生时间：YYYYMMdd

    /**
     * 历史消息的处理状态
     *
     * @see HistoryMessageStatus
     */
    private byte status;

    private byte retryCount; // 重试次数，重试一定次数后仍旧失败的话，其状态应更新为3

    private String failedReason; // 入库失败的原因描述：下载失败(download)/解压失败(unzip)/写入失败(insert)

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHistUrl() {
        return histUrl;
    }

    public void setHistUrl(String histUrl) {
        this.histUrl = histUrl;
    }

    public int getHistDate() {
        return histDate;
    }

    public void setHistDate(int histDate) {
        this.histDate = histDate;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(byte retryCount) {
        this.retryCount = retryCount;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}