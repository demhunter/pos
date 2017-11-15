/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import java.io.Serializable;

/**
 * IM站内消息接收者的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/11/4
 */
public class InternalMessageReceiver implements Serializable {

    private static final long serialVersionUID = -1566961168008602507L;

    private Long id;

    private Long msgId; // 站内消息ID

    private Long userId; // 消息接收者ID

    private String userType; // 消息接收者的用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员',

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}