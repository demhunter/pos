/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import com.pos.basic.constant.MQMessageType;

import java.util.Date;

/**
 * Message
 *
 * @author cc
 * @version 1.0, 2017/1/17
 */
public class Message {

    /**
     * 自增id
     */
    private Long id;

    /**
     * 消息类型 {@link MQMessageType#code}
     */
    private Byte type;

    /**
     * 交换器
     */
    private String exchange;

    /**
     * 路由键
     */
    private String routeKey;

    /**
     * 数据（Json序列化）
     */
    private String data;

    /**
     * 序列号，UUID
     */
    private String serialNumber;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 对应于 {@link MQMessageType#PRODUCER} 为消息发送处理类类名
     * 对应于 {@link MQMessageType#CONSUMER} 为消息接收处理类类名
     */
    private String handleClass;

    /**
     * 对应于 {@link MQMessageType#PRODUCER} 为消息发送处理是否成功
     * 对应于 {@link MQMessageType#CONSUMER} 为消息接收处理是否成功
     * true：消息处理成功；false：消息处理失败（进一步查看消息处理是否存在异常）
     */
    private Boolean handledSuccess;

    /**
     * 对应于 {@link MQMessageType#PRODUCER} 为消息发送处理是否存在异常
     * 对应于 {@link MQMessageType#CONSUMER} 为消息接收处理是否存在异常
     * false：消息处理不存在异常，true：消息处理存在异常
     */
    private Boolean existedException;

    public String getHandleClass() {
        return handleClass;
    }

    public void setHandleClass(String handleClass) {
        this.handleClass = handleClass;
    }

    public Boolean getHandledSuccess() {
        return handledSuccess;
    }

    public void setHandledSuccess(Boolean handledSuccess) {
        this.handledSuccess = handledSuccess;
    }

    public Boolean getExistedException() {
        return existedException;
    }

    public void setExistedException(Boolean existedException) {
        this.existedException = existedException;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
