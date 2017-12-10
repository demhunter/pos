/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

import com.pos.basic.dto.operation.mq.OperationMsg;
import com.pos.basic.mq.MQReceiverType;

/**
 * 操作日志Service
 *
 * @author wangbing
 * @version 1.0, 2017/12/9
 */
public interface OperationLogService {

    /**
     * 保存操作信息
     *
     * @param operationMsg 操作信息
     */
    void saveOperationLog(OperationMsg operationMsg);

    /**
     * 发送记录操作日志消息
     *
     * @param operationMsg 操作消息
     * @param receiverType 消息接收方
     */
    void sendOperationMsg(OperationMsg operationMsg, MQReceiverType receiverType);
}
