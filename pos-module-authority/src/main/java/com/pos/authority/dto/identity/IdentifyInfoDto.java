/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.identity;

import com.pos.authority.fsm.context.AuditStatusTransferContext;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/18
 */
public class IdentifyInfoDto implements Serializable {

    private static final long serialVersionUID = -6211315242671389153L;
    private Long userId; // 被审核用户id

    private boolean allowed; // 审核结果：true = 通过审核，false = 不通过审核

    private String rejectReason; // 不通过原因，当allowed = false时，此字段必填

    private Long operatorUserId; // 审核操作人userId

    private Date updateKey; // 审核操作Key

    /**
     * 参数字段校验
     *
     * @param fieldPrefix 提示前缀
     * @throws IllegalParamException 参数校验异常，参数不合法
     */
    public void check(String fieldPrefix) throws IllegalParamException {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(userId, fieldPrefix + "userId");
        FieldChecker.checkEmpty(operatorUserId, fieldPrefix + "operatorUserId");
        FieldChecker.checkEmpty(updateKey, fieldPrefix + "updateKey");
    }

    public AuditStatusTransferContext buildStatusTransferContext() {
        AuditStatusTransferContext transferContext;
        if (isAllowed()) {
            transferContext = new AuditStatusTransferContext(userId, operatorUserId);
        } else {
            FieldChecker.checkMinMaxLength(rejectReason, 1, 50, "rejectReason");
            transferContext = new AuditStatusTransferContext(userId, operatorUserId, rejectReason);
        }
        return transferContext;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Long getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(Long operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    public Date getUpdateKey() {
        return updateKey;
    }

    public void setUpdateKey(Date updateKey) {
        this.updateKey = updateKey;
    }

    public void setUpdateKey(String updateKey) {
        try {
            Long timeStamp = Long.valueOf(updateKey);
            this.updateKey = new Date(timeStamp);
        } catch (Exception e) {
            throw new IllegalParamException("updateKey参数格式不正确");
        }


    }
}
