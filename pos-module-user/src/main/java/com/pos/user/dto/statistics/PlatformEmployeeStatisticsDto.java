/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.statistics;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/7/12
 */
public class PlatformEmployeeStatisticsDto implements Serializable {
    private static final long serialVersionUID = -8567711831095406864L;

    private Long id;

    private Long userId;

    private int customerTalking;//谈单中的客户数

    private int customerComplete;//已成单客户数

    private int customerRefuse;//已飞单客户数

    private int imSessionAvailable;//未关闭会话数

    private int imSessionClosed;//已关闭会话数

    private int orderUnpaid;//待支付订单数

    private int orderGoing;//进行中的订单数

    private int orderFinished;//已完成的订单数

    private int orderTerminated;//已关闭的订单数

    private int twitter;//关联的推客数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getCustomerTalking() {
        return customerTalking;
    }

    public void setCustomerTalking(int customerTalking) {
        this.customerTalking = customerTalking;
    }

    public int getCustomerComplete() {
        return customerComplete;
    }

    public void setCustomerComplete(int customerComplete) {
        this.customerComplete = customerComplete;
    }

    public int getCustomerRefuse() {
        return customerRefuse;
    }

    public void setCustomerRefuse(int customerRefuse) {
        this.customerRefuse = customerRefuse;
    }

    public int getImSessionAvailable() {
        return imSessionAvailable;
    }

    public void setImSessionAvailable(int imSessionAvailable) {
        this.imSessionAvailable = imSessionAvailable;
    }

    public int getImSessionClosed() {
        return imSessionClosed;
    }

    public void setImSessionClosed(int imSessionClosed) {
        this.imSessionClosed = imSessionClosed;
    }

    public int getOrderUnpaid() {
        return orderUnpaid;
    }

    public void setOrderUnpaid(int orderUnpaid) {
        this.orderUnpaid = orderUnpaid;
    }

    public int getOrderGoing() {
        return orderGoing;
    }

    public void setOrderGoing(int orderGoing) {
        this.orderGoing = orderGoing;
    }

    public int getOrderFinished() {
        return orderFinished;
    }

    public void setOrderFinished(int orderFinished) {
        this.orderFinished = orderFinished;
    }

    public int getOrderTerminated() {
        return orderTerminated;
    }

    public void setOrderTerminated(int orderTerminated) {
        this.orderTerminated = orderTerminated;
    }

    public int getTwitter() {
        return twitter;
    }

    public void setTwitter(int twitter) {
        this.twitter = twitter;
    }
}
