/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.request;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/8/27
 */
public class UpdateRemarkDto implements Serializable {

    private static final long serialVersionUID = -1044801293703709241L;
    @ApiModelProperty("渠道商用户ID")
    private long channelUserId;

    @ApiModelProperty("备注")
    private String remark;

    public long getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(long channelUserId) {
        this.channelUserId = channelUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
