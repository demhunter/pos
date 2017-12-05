/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.fsm.context;

/**
 * AuditStatusTransferContext，参见sm_pos_audit.xml
 *
 * @author wangbing
 * @version 1.0, 2017/10/17
 */
public class AuditStatusTransferContext {

    // user_pos_auth主键id
    private Long posAuthId;

    // 状态变更操作人userId
    private Long operatorUserId;

    // 如果审核不通过，此字段为不通过原因
    private String rejectReason;

    public AuditStatusTransferContext(Long posAuthId, Long operatorUserId) {
        this.posAuthId = posAuthId;
        this.operatorUserId = operatorUserId;
    }

    public AuditStatusTransferContext(Long posAuthId, Long operatorUserId, String rejectReason) {
        this.posAuthId = posAuthId;
        this.operatorUserId = operatorUserId;
        this.rejectReason = rejectReason;
    }

    public Long getPosAuthId() {
        return posAuthId;
    }

    public void setPosAuthId(Long posAuthId) {
        this.posAuthId = posAuthId;
    }

    public Long getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(Long operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    public String getRejectReason() {
        return rejectReason;
    }
}
