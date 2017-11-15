/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * IM会话的消息记录的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/10/20
 */
public class SessionMessage implements Serializable {

    private Long id;

    private Long sessionId; // 会话ID

    private Long userId; // 消息发送者ID

    private String userType; // 消息发送者的用户类型：c = C端用户，b = B端用户，e = B端从业者，m = 平台管理员

    private String msgType; // 消息类型，参见对接IM平台的类型值

    private String content; // 消息内容，参见对接IM平台的数据格式

    private Date sendTime; // 消息发送时间

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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

}