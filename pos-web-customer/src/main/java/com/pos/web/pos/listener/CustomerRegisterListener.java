/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.listener;

import com.pos.basic.mq.AbstractMQListener;
import com.pos.basic.mq.MQMessage;
import com.pos.common.util.validation.Preconditions;
import com.pos.pos.service.PosService;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.mq.CustomerInfoMsg;
import com.pos.user.service.CustomerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 客户注册成功，初始化权限和关联推客关系Listener
 *
 * @author wangbing
 * @version 1.0, 2017/11/21
 */
@Component
public class CustomerRegisterListener extends AbstractMQListener {

    @Resource
    private PosService posService;

    @Resource
    private CustomerService customerService;

    @Override
    public boolean messageHandler(MQMessage message) {
        Preconditions.checkArgsNotNull(message);
        CustomerInfoMsg msg = extractData(message, CustomerInfoMsg.class);

        CustomerDto customerDto = customerService.findById(msg.getUserId(), false, false);
        if (customerDto != null) {
            posService.posLogin(customerDto, msg.getRecommendType(), msg.getRecommendId());
        }

        return true;
    }

    @Override
    @RabbitListener(queues = "pos.customer.reg.queue")
    public void onListen(MQMessage message) {
        super.onListen(message);
    }
}
