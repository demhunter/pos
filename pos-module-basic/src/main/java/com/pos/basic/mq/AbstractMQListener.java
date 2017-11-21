/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.mq;

import com.pos.basic.constant.MQConstant;
import com.pos.basic.constant.MQMessageType;
import com.pos.basic.dao.MQMessageDao;
import com.pos.basic.domain.Message;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.validation.Preconditions;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * AbstractMQListener
 *
 * @author cc
 * @version 1.0, 2017/1/17
 */
@Component
public class AbstractMQListener implements IMQListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMQListener.class);

    @Resource
    private MQMessageDao mqMessageDao;

    private void doRecord(MQMessage message, boolean handledSuccess, boolean existedException) {
        Message msg = new Message();

        msg.setExchange(message.getReceiverType().getDesc() + MQConstant.DOT + message.getExchangeType());
        msg.setRouteKey(message.getRouteKey());
        msg.setType(MQMessageType.CONSUMER.getCode());
        msg.setData(JsonUtils.objectToJson(message.getData()));
        msg.setSerialNumber(message.getSerialNumber());
        msg.setCreateTime(new Date());
        msg.setHandleClass(this.getClass().toString());
        msg.setHandledSuccess(handledSuccess);
        msg.setExistedException(existedException);

        mqMessageDao.saveMessage(msg);
    }

    public void onListen(MQMessage message) {
        Preconditions.checkArgsNotNull(message, message.getReceiverType());

        boolean handledSuccess = true; // 消息处理默认成功
        boolean existedException = false; // 消息处理默认不存在异常
        try {
            handledSuccess = this.messageHandler(message);
        } catch (Exception e) {
            logger.error("消息{}处理异常！Exception={}", message, e);
            handledSuccess = false;
            existedException = true;
        } finally {
            doRecord(message, handledSuccess, existedException);
        }
    }

    /**
     * 从消息中获取数据——数据对象是非范型时使用
     *
     * @param message 消息体
     * @param dataClass 数据对象类型
     * @param <T> T
     * @return 数据对象
     */
    public <T> T extractData(MQMessage message, Class<T> dataClass) {
        Preconditions.checkArgsNotNull(message, dataClass);

        String dataJson = JsonUtils.objectToJson(message.getData());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(dataJson, dataClass);
        } catch (IOException e) {
            logger.error("反序列化消息数据失败！message={}", message, e);
            throw new IllegalStateException("反序列化消息数据失败！");
        }
    }

    /**
     * 从消息中获取数据——数据对象是范型时使用，如List<String>
     *
     * @param message 消息体
     * @param typeReference 数据对象类型
     * @param <T> T
     * @return 数据对象
     */
    public <T> T extractData(MQMessage message, TypeReference<T> typeReference) {
        Preconditions.checkArgsNotNull(message, typeReference);

        String dataJson = JsonUtils.objectToJson(message.getData());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(dataJson, typeReference);
        } catch (IOException e) {
            logger.error("反序列化消息数据失败！message={}", message, e);
            throw new IllegalStateException("反序列化消息数据失败！");
        }
    }

    @Override
    public boolean messageHandler(MQMessage message) {
        // 子类重写该方法来处理监听逻辑
        throw new UnsupportedOperationException("监听处理逻辑调用错误！");
    }
}
