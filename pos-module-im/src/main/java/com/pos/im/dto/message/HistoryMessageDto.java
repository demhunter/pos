/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.message;

import com.pos.im.constant.HistoryMessageStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * IM历史消息的处理记录的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/10/24
 */
public class HistoryMessageDto implements Serializable {

    private Long id;

    /**
     * 历史消息的下载地址
     */
    private String histUrl;

    /**
     * 历史消息的产生时间：YYYYMMdd
     */
    private int histDate;

    /**
     * 历史消息的处理状态(int)
     *
     * @see HistoryMessageStatus
     */
    private byte status;

    /**
     * 消息入库的重试次数(int)
     */
    private byte retryCount;

    /**
     * 入库失败的原因描述
     */
    private String failedReason;

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