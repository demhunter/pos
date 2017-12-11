/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.failure;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 交易失败信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
public class TransactionFailureRecordDto implements Serializable {

    @ApiModelProperty("自增主键id")
    private Long id;

    @ApiModelProperty("交易id")
    private Long transactionId;

    @ApiModelProperty("交易失败原因")
    private String failureReason;

    @ApiModelProperty("交易失败时间")
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
