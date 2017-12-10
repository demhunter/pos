/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.pos.basic.constant.OperationType;
import com.pos.basic.dao.OperationLogDao;
import com.pos.basic.domain.OperationLog;
import com.pos.basic.dto.operation.mq.OperationMsg;
import com.pos.basic.mq.MQMessage;
import com.pos.basic.mq.MQReceiverType;
import com.pos.basic.mq.MQTemplate;
import com.pos.basic.service.OperationLogService;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.basic.UUIDUnsigned32;
import com.pos.common.util.validation.FieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 操作日志ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/12/9
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OperationLogServiceImpl implements OperationLogService {

    private static final Logger LOG = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    @Resource
    private MQTemplate mqTemplate;

    @Resource
    private OperationLogDao operationLogDao;

    @Override
    public void saveOperationLog(OperationMsg operationMsg) {
        FieldChecker.checkEmpty(operationMsg, "operationMsg");
        operationMsg.check();

        LOG.info("收到一条记录操作日志的消息");
        OperationLog operationLog = buildOperationLog(operationMsg);
        operationLog.catalinaOut(LOG);

        operationLogDao.save(operationLog);
    }

    private OperationLog buildOperationLog(OperationMsg operationMsg) {
        OperationLog operationLog = new OperationLog();

        operationLog.setUuid(UUIDUnsigned32.randomUUIDString());
        operationLog.setUserId(operationMsg.getOperator().getUserId());
        operationLog.setUserType(operationMsg.getOperator().getUserType());
        operationLog.setOperationType(operationMsg.getOperationType());
        operationLog.setOperationDetailType(operationMsg.getOperationDetailType());
        operationLog.setOperationName(operationMsg.parseSubOperation().getName());
        operationLog.setSucc(operationMsg.getOperateSucc());
        if (!operationMsg.getOperateSucc()) {
            operationLog.setFailureContent(JsonUtils.objectToJson(operationMsg.getFailureContent()));
        }

        return operationLog;
    }

    @Override
    public void sendOperationMsg(OperationMsg operationMsg, MQReceiverType receiverType) {
        FieldChecker.checkEmpty(operationMsg, "operationMsg");
        FieldChecker.checkEmpty(receiverType, "receiverType");
        operationMsg.check();

        mqTemplate.sendDirectMessage(new MQMessage(receiverType, "pos.log.operation.route.key", operationMsg));
        LOG.info("发送一条记录操作日志的消息");
    }
}
