/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.fsm.handler.transaction;

import com.pos.basic.sm.action.FSMAction;
import com.pos.pos.constants.TransactionStatusType;
import com.pos.pos.service.PosService;
import com.pos.pos.fsm.context.TransactionStatusTransferContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * PosTransactionHandledSuccessHandler
 *
 * @author wangbing
 * @version 1.0, 2017/10/19
 */
@Component
public class PosTransactionHandledSuccessHandler extends FSMAction {

    @Resource
    private PosService posService;

    @Override
    @SuppressWarnings("all")
    public boolean action(String fromState, String toState, Object context) {
        TransactionStatusTransferContext statusTransfer = (TransactionStatusTransferContext) context;
        TransactionStatusType targetStatus = TransactionStatusType.getEnum(toState);

        // 交易状态变更
        posService.updateTransactionStatus(statusTransfer, targetStatus);
        // 计算佣金
        posService.generateBrokerage(statusTransfer.getRecordId());

        return true;
    }
}
