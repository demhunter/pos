/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.condition.query;

import com.pos.common.util.date.SimpleDateUtils;
import com.pos.im.constant.InternalMessageType;

import java.io.Serializable;
import java.util.Date;

import static com.pos.common.util.date.SimpleDateUtils.DatePattern.YYYYMMDD;
import static com.pos.common.util.date.SimpleDateUtils.HourMinSecondFormat.TODAY_END;

/**
 * 站内消息的查询条件.
 *
 * @author wayne
 * @version 1.0, 2016/11/7
 */
public class InternalMessageCondition implements Serializable {

    private Long receiverId; // 消息接收者的用户ID

    private String receiverType; // 消息接收者的用户类型

    private byte[] msgTypes; // 消息类型（如果为空，为了优化查询索引，将返回所有类型）

    private Date sendBeginTime; // 消息发送的开始时间（可空）

    private Date sendEndTime; // 消息发送的结束时间（可空）

    public void parseAndSetMsgType(Integer msgType) {
        if (msgType != null) {
            msgTypes = new byte[]{msgType.byteValue()};
        }
    }

    @SuppressWarnings("all")
    public void parseAndSetSendBeginTime(String sendBeginTime) {
        this.sendBeginTime = SimpleDateUtils.parseDate(sendBeginTime, YYYYMMDD.toString());
    }

    @SuppressWarnings("all")
    public void parseAndSetSendEndTime(String sendEndTime) {
        this.sendEndTime = SimpleDateUtils.parseDate(sendEndTime, YYYYMMDD.toString(), TODAY_END);
    }

    public byte[] getMsgTypes() {
        if (msgTypes != null && msgTypes.length > 0) {
            return msgTypes;
        } else {
            byte[] allTypes = new byte[InternalMessageType.values().length];
            for (int i = 0; i < InternalMessageType.values().length; i++) {
                allTypes[i] = InternalMessageType.values()[i].getCode();
            }
            return allTypes;
        }
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public void setMsgTypes(byte[] msgTypes) {
        this.msgTypes = msgTypes;
    }

    public Date getSendBeginTime() {
        return sendBeginTime;
    }

    public void setSendBeginTime(Date sendBeginTime) {
        this.sendBeginTime = sendBeginTime;
    }

    public Date getSendEndTime() {
        return sendEndTime;
    }

    public void setSendEndTime(Date sendEndTime) {
        this.sendEndTime = sendEndTime;
    }

}