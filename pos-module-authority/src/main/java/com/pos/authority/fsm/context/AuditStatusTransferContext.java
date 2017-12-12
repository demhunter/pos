/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.fsm.context;

import java.io.Serializable;

/**
 * AuditStatusTransferContext，参见sm_pos_audit.xml
 *
 * @author wangbing
 * @version 1.0, 2017/10/17
 */
public class AuditStatusTransferContext implements Serializable {

    private static final long serialVersionUID = -7181779850373467544L;
    // user_pos_auth主键id
    private Long posAuthId;

    // 权限相关用户id
    private Long userId;

    // 状态变更操作人userId
    private Long operatorUserId;

    // 如果审核不通过，此字段为不通过原因
    private String rejectReason;

    public AuditStatusTransferContext(Long posAuthId, Long operatorUserId) {
        this.posAuthId = posAuthId;
        this.operatorUserId = operatorUserId;
    }

    public AuditStatusTransferContext(Long posAuthId, Long userId, Long operatorUserId) {
        this.posAuthId = posAuthId;
        this.userId = userId;
        this.operatorUserId = operatorUserId;
    }

    public AuditStatusTransferContext(Long posAuthId, Long operatorUserId, String rejectReason) {
        this.posAuthId = posAuthId;
        this.operatorUserId = operatorUserId;
        this.rejectReason = rejectReason;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
