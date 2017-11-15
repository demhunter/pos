/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.statistics;

import java.io.Serializable;

/**
 *
 * 家居顾问相关的客户的统计数据信息
 * @author 睿智
 * @version 1.0, 2017/7/12
 */
public class PlatformCustomerStatisticsDto implements Serializable {

    private static final long serialVersionUID = -7411377220343181902L;

    private Long id;

    private Long userId;

    private int imSessionAvailable;//未关闭会话数

    private int imSessionClosed;//已关闭会话数

    private int orderUnpaid;//待支付订单数

    private int orderGoing;//进行中的订单数

    private int orderFinished;//已完成订单数

    private int orderTerminated;//已关闭订单数

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
}
