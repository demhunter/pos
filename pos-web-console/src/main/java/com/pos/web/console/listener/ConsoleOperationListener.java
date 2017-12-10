/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.listener;

import com.pos.basic.dto.operation.mq.OperationMsg;
import com.pos.basic.mq.AbstractMQListener;
import com.pos.basic.mq.MQMessage;
import com.pos.basic.service.OperationLogService;
import com.pos.common.util.validation.Preconditions;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 后台操作日志记录Listener
 *
 * @author wangbing
 * @version 1.0, 2017/12/9
 */
@Component
public class ConsoleOperationListener extends AbstractMQListener {

    @Resource
    private OperationLogService operationLogService;

    @Override
    @RabbitListener(queues = "pos.log.operation.queue")
    public void onListen(MQMessage message) {
        super.onListen(message);
    }

    @Override
    public boolean messageHandler(MQMessage message) {
        Preconditions.checkArgsNotNull(message);
        OperationMsg operationMsg = extractData(message, OperationMsg.class);

        operationLogService.saveOperationLog(operationMsg);

        return true;
    }
}
