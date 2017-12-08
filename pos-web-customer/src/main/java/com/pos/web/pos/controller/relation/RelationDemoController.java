/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.relation;

import com.pos.authority.service.support.relation.CustomerRelationNode;
import com.pos.authority.service.support.relation.CustomerRelationTree;
import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.basic.mq.MQMessage;
import com.pos.basic.mq.MQReceiverType;
import com.pos.basic.mq.MQTemplate;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.user.dto.mq.CustomerInfoMsg;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Stack;

/**
 * 关系DemoController
 *
 * @author wangbing
 * @version 1.0, 2017/11/24
 */
@RestController
@RequestMapping("/relation")
@Api(value = "/relation", description = "v2.0.0 * 关系demo接口")
public class RelationDemoController {

    @Resource
    private CustomerRelationPoolSupport customerRelationPoolSupport;

    @Resource
    private MQTemplate mqTemplate;

    private final static Logger logger = LoggerFactory.getLogger(RelationDemoController.class);

    @RequestMapping(value = "register/message", method = RequestMethod.GET)
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
        mqTemplate.sendDirectMessage(new MQMessage(MQReceiverType.POS, "pos.reg.route.key", msg));
        logger.info("发送一条用户注册的消息");
    }

    @RequestMapping(value = "participation/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取参与分佣的用户栈", notes = "成功返回，则栈顶一定为交易用户信息，栈底一定为根节点")
    public ApiResult<Stack<CustomerRelationNode>> brokerageParticipation(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId) {
        // return ApiResult.succ(customerRelationPoolSupport.getParticipationForBrokerage(userId));
        return ApiResult.succ();
    }
}
