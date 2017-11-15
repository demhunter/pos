/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.mq;

/**
 * IMQTemplate
 *
 * @author cc
 * @version 1.0, 2017/1/16
 */
public interface IMQTemplate {

    /**
     * 发送direct消息
     *
     * @param message 消息体
     */
    void sendDirectMessage(MQMessage message);

    /**
     * 发送topic消息
     *
     * @param message 消息体
     */
    void sendTopicMessage(MQMessage message);

    /**
     * 发送fanout消息
     *
     * @param message 消息体
     */
    void sendFanoutMessage(MQMessage message);
}
