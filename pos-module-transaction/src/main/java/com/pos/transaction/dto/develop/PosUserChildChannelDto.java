/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.develop;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 下级推客列表信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class PosUserChildChannelDto extends PosUserSimpleChannelDto implements Serializable {

    @ApiModelProperty("备注")
    private String channelRemark;

    @ApiModelProperty("下级推客姓名")
    private String childChannelName;

    @ApiModelProperty("为上级推客提供的累计佣金（BigDecimal）")
    private BigDecimal brokerage;

    @ApiModelProperty("身份认证状态：0 = 未提交，1 = 未审核，2 = 已通过，3 = 未通过")
    private Integer identityAuditStatus;

    public Integer getIdentityAuditStatus() {
        return identityAuditStatus;
    }

    public void setIdentityAuditStatus(Integer identityAuditStatus) {
        this.identityAuditStatus = identityAuditStatus;
    }

    public String getChannelRemark() {
        return channelRemark;
    }

    public void setChannelRemark(String channelRemark) {
        this.channelRemark = channelRemark;
    }

    public String getChildChannelName() {
        return childChannelName;
    }

    public void setChildChannelName(String childChannelName) {
        this.childChannelName = childChannelName;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }
}
