/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.mq;

import com.pos.basic.constant.MQConstant;
import com.pos.basic.dao.MQMessageDao;
import com.pos.basic.domain.Message;
import com.pos.basic.constant.MQMessageType;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 消息队列模版
 *
 * @author cc
 * @version 1.0, 2017/1/16
 */
@Component
public class MQTemplate implements IMQTemplate {

    private static final Logger logger = LoggerFactory.getLogger(MQTemplate.class);

    /**
     * Spring Direct类型模板
     */
    @Resource(name = "directTemplate")
    private RabbitTemplate directTemplate;

    /**
     * Spring Topic类型模板
     */
    @Resource(name = "topicTemplate")
    private RabbitTemplate topicTemplate;

    /**
     * Spring Fanout类型模板
     */
    @Resource(name = "fanoutTemplate")
    private RabbitTemplate fanoutTemplate;

    @Resource
    private MQMessageDao mqMessageDao;

    private void sendMessage(MQMessage mqMessage, String exchangePostfix) {
        Preconditions.checkArgsNotNull(exchangePostfix);

        RabbitTemplate rabbitTemplate;
        switch (exchangePostfix) {
        case MQConstant.DIRECT_EXCHANGE:
            rabbitTemplate = directTemplate;
            mqMessage.setExchangeType(MQConstant.DIRECT_EXCHANGE);
            break;
        case MQConstant.TOPIC_EXCHANGE:
            rabbitTemplate = topicTemplate;
            mqMessage.setExchangeType(MQConstant.TOPIC_EXCHANGE);
            break;
        case MQConstant.FANOUT_EXCHANGE:
            rabbitTemplate = fanoutTemplate;
            mqMessage.setExchangeType(MQConstant.FANOUT_EXCHANGE);
            break;
        default:
            throw new IllegalParamException("非法的参数！");
        }

        boolean handledSuccess = true; // 消息处理默认成功
        boolean existedException = false; // 消息处理默认不存在异常
        try {
            rabbitTemplate.convertAndSend(mqMessage.getReceiverType().getDesc() + MQConstant.DOT + exchangePostfix, mqMessage.getRouteKey(), mqMessage);
        } catch (Exception e) {
            logger.error("消息发送失败，mqMessage={}，exchange={}", mqMessage, exchangePostfix, e);
            handledSuccess = false;
            existedException = true;
        } finally {
            Message message = new Message();

            message.setExchange(mqMessage.getReceiverType().getDesc() + MQConstant.DOT + exchangePostfix);
            message.setRouteKey(mqMessage.getRouteKey());
            message.setType(MQMessageType.PRODUCER.getCode());
            message.setData(JsonUtils.objectToJson(mqMessage.getData()));
            message.setSerialNumber(mqMessage.getSerialNumber());
            message.setCreateTime(new Date());
            message.setHandleClass(this.getClass().toString());
            message.setHandledSuccess(handledSuccess);
            message.setExistedException(existedException);

            mqMessageDao.saveMessage(message);
        }
    }

    @Override
    public void sendDirectMessage(MQMessage message) {
        Preconditions.checkArgsNotNull(message, message.getReceiverType(), message.getRouteKey());// 消息内容允许为空

        sendMessage(message, MQConstant.DIRECT_EXCHANGE);
    }

    @Override
    public void sendTopicMessage(MQMessage message) {
        Preconditions.checkArgsNotNull(message, message.getReceiverType(), message.getRouteKey());// 消息内容允许为空

        sendMessage(message, MQConstant.TOPIC_EXCHANGE);
    }

    @Override
    public void sendFanoutMessage(MQMessage message) {
        Preconditions.checkArgsNotNull(message, message.getReceiverType(), message.getRouteKey());// 消息内容允许为空

        sendMessage(message, MQConstant.FANOUT_EXCHANGE);
    }
}
