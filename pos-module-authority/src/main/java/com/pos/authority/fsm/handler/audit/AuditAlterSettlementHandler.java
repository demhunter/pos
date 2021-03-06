/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.fsm.handler.audit;

import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.fsm.context.AuditStatusTransferContext;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.basic.sm.action.FSMAction;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * AuditAlterSettlementHandler
 *
 * @author wangbing
 * @version 1.0, 2017/12/6
 */
@Component
public class AuditAlterSettlementHandler extends FSMAction {

    @Resource
    private CustomerAuthorityService customerAuthorityService;

    @Override
    public boolean action(String fromState, String toState, Object context) {
        AuditStatusTransferContext transferContext = (AuditStatusTransferContext) context;
        CustomerAuditStatus auditStatus = CustomerAuditStatus.getEnum(toState);

        return customerAuthorityService.updateAuditStatus(transferContext, auditStatus);
    }
}
