/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import java.io.Serializable;

/**
 * IM会话关联的订单领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/12/12
 */
public class SessionOrder implements Serializable {

    private static final long serialVersionUID = 981697015990724808L;

    private Long id;

    private Long sessionId; // 会话ID

    private Long orderId; // 会话关联的订单ID

    private Long orderNum; // 会话关联的订单编号

    private Byte orderContentType; // 会话关联订单的内容类型

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public Byte getOrderContentType() {
        return orderContentType;
    }

    public void setOrderContentType(Byte orderContentType) {
        this.orderContentType = orderContentType;
    }

}