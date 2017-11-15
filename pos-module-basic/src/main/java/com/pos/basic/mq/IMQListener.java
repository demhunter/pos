/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.mq;

/**
 * IMQListener
 *
 * @author cc
 * @version 1.0, 2017/1/17
 */
public interface IMQListener {

    /**
     * 监听实现
     *
     * @param message 消息体
     */
    boolean messageHandler(MQMessage message);
}
