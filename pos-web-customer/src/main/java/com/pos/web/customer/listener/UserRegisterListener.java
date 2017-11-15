/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.listener;

import com.pos.basic.mq.AbstractMQListener;
import com.pos.basic.mq.MQMessage;
import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.service.AuthorityService;
import com.pos.user.dto.mq.CustomerInfoMsg;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户注册消息监听Listener
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
@Component
public class UserRegisterListener extends AbstractMQListener {

    @Resource
    private AuthorityService authorityService;

    @Override
    @RabbitListener(queues = "pos.user.register.queue")
    public void onListen(MQMessage message) {
        super.onListen(message);
    }

    @Override
    public boolean messageHandler(MQMessage message) {
        FieldChecker.checkEmpty(message, "message");
        CustomerInfoMsg msg = extractData(message, CustomerInfoMsg.class);

        authorityService.initializeAuthority(msg.getUserId(), msg.getRegisterRecommend());

        return true;
    }
}
