/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.relation;

import com.pos.authority.service.support.relation.CustomerRelationTree;
import com.pos.basic.constant.OperationType;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.operation.mq.OperationMsg;
import com.pos.basic.mq.MQMessage;
import com.pos.basic.mq.MQReceiverType;
import com.pos.basic.mq.MQTemplate;
import com.pos.basic.service.OperationLogService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.user.dto.mq.CustomerInfoMsg;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 关系DemoController
 *
 * @author wangbing
 * @version 1.0, 2017/11/24
 */
/*@RestController
@RequestMapping("/relation")
@Api(value = "/relation", description = "v2.0.0 * 关系demo接口")*/
@Deprecated
public class RelationDemoController {

    @Resource
    private MQTemplate mqTemplate;

    @Resource
    private OperationLogService operationLogService;

    private final static Logger logger = LoggerFactory.getLogger(RelationDemoController.class);

    /*@RequestMapping(value = "register/message", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 发送注册MQ消息", notes = "发送注册MQ消息")
    public ApiResult<CustomerRelationTree> explain(
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "userPhone", required = false) String userPhone,
            @RequestParam(name = "recommendUserId", required = false) Long recommendUserId,
            @RequestParam(name = "recommendType", required = false) Byte recommendType) {
        sendCustomerRegisterMessage(userId, userPhone, recommendUserId, recommendType);
        return ApiResult.succ();
    }

    private void sendCustomerRegisterMessage(Long userId, String userPhone, Long recommendUserId, Byte recommendType) {
        CustomerInfoMsg msg = new CustomerInfoMsg(userId, userPhone, recommendUserId, recommendType);
        mqTemplate.sendDirectMessage(new MQMessage(MQReceiverType.POS_CUSTOMER, "pos.reg.route.key", msg));
        logger.info("发送一条用户注册的消息");
    }

    @RequestMapping(value = "operation/message/succ", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 发送操作成功消息", notes = "发送操作成功消息")
    public ApiResult<CustomerRelationTree> operationMessage(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "operationType") int operationType,
            @RequestParam(name = "operationDetailType") int operationDetailType) {
        UserIdentifier userIdentifier = new UserIdentifier(userId, "c");
        OperationType.SubOperationType operation = OperationType.getSubOperation(operationType, operationDetailType);

        OperationMsg operationMsg = OperationMsg.create(userIdentifier, operation);
        operationMsg.operateSuccess();

        operationLogService.sendOperationMsg(operationMsg, MQReceiverType.POS_CUSTOMER);

        return ApiResult.succ();
    }

    public static void main(String[] args) {
        first();
    }

    public static void first() {
        second();
    }
    public static void second() {
        try {
            third();
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.toString());
            System.out.println("在调用第三步时发生异常");
        }
    }
    public static void third() throws Exception {
        throw new Exception("第三步--发生异常！");
    }*/
}
