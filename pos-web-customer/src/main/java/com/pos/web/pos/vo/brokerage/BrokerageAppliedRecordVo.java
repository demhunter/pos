/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.brokerage;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 佣金提现记录
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class BrokerageAppliedRecordVo implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("提现金额")
    private BigDecimal amount;

    @ApiModelProperty("提现状态")
    private Integer status;

    @ApiModelProperty("提现状态描述")
    private String statusDesc;

    @ApiModelProperty("提现时间")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
