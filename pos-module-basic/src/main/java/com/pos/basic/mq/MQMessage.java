/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.mq;

import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.basic.UUIDUnsigned32;

import java.io.Serializable;

/**
 * MQMessage
 *
 * @author cc
 * @version 1.0, 2017/1/16
 */
public class MQMessage implements Serializable {

    /**
     * 消息接收方的（容器）类型
     */
    private MQReceiverType receiverType;

    /**
     * 路由键，配置在消息发送方
     */
    private String routeKey;

    /**
     * 数据（消息内容，是一个未被序列化的对象）
     */
    private Object data;

    /**
     * 交换器类型（在发送消息时不需要设置该数据）
     */
    private String exchangeType;

    /**
     * 序列号，UUID
     */
    private String serialNumber;

    public MQMessage() {
    }

    public MQMessage(MQReceiverType receiverType, String routeKey, Object data) {
        this.receiverType = receiverType;
        this.routeKey = routeKey;
        this.data = data;
        this.serialNumber = UUIDUnsigned32.randomUUIDString();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public MQReceiverType getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(MQReceiverType receiverType) {
        this.receiverType = receiverType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
