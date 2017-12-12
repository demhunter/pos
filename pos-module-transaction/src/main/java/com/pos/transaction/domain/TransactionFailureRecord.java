/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.domain;

import java.io.Serializable;

/**
 * 交易失败信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class TransactionFailureRecord implements Serializable {

    private static final long serialVersionUID = -7110518141485438288L;
    private Long id; // 自增主键id

    private Long transactionId; // 交易id

    private String failureReason; // 交易失败原因

    private String createTime; // 交易失败时间

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
