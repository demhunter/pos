/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.fsm.context;

/**
 * TransactionStatusTransferContext，参见sm_pos_transaction.xml
 *
 * @author wangbing
 * @version 1.0, 2017/10/20
 */
public class TransactionStatusTransferContext {

    private Long recordId; // 交易记录id

    private String serialNumber; // 流水号

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
