/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.mq;

import com.pos.basic.dto.UserIdentifier;

import java.io.Serializable;

/**
 * 客戶推荐注册的消息对象.
 *
 * @author wayne
 * @version 1.0, 2017/1/6
 */
public class CustomerRecommendMsg implements Serializable {

    private static final long serialVersionUID = 5086701066329179618L;

    private Long customerUserId; // 注册客户ID

    private UserIdentifier recommend; // 推荐人标识

    public Long getCustomerUserId() {
        return customerUserId;
    }

    public void setCustomerUserId(Long customerUserId) {
        this.customerUserId = customerUserId;
    }

    public UserIdentifier getRecommend() {
        return recommend;
    }

    public void setRecommend(UserIdentifier recommend) {
        this.recommend = recommend;
    }

}