/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.fsm.handler.audit;

import com.pos.basic.sm.action.FSMAction;
import com.pos.pos.constants.UserAuditStatus;
import com.pos.pos.fsm.context.AuditStatusTransferContext;
import com.pos.pos.service.PosService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * PosUserSubmitAllHandler
 *
 * @author wangbing
 * @version 1.0, 2017/10/17
 */
@Component
public class PosUserSubmitAllHandler extends FSMAction {

    @Resource
    private PosService posService;

    @Override
    public boolean action(String fromState, String toState, Object context) {
        AuditStatusTransferContext transferContext = (AuditStatusTransferContext) context;
        UserAuditStatus auditStatus = UserAuditStatus.getEnum(toState);

        return posService.updateAuditStatus(transferContext, auditStatus);
    }

}
