/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 网易IM消息抄送的数据
 * @author 睿智
 * @version 1.0, 2017/9/21
 */
public class NeteaseMessage implements Serializable {

    private static final long serialVersionUID = 4409165375032773809L;

    private long id;

    private String eventType;

    private String convType;

    private String to;

    private String fromAccount;

    private Date sendTime;

    private String msgType;

    private String attach;

    private String body;

    private String msgClientId;

    private String msgServerId;

    private String customApnsText;

    private String tMembers;

    private String ext;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getConvType() {
        return convType;
    }

    public void setConvType(String convType) {
        this.convType = convType;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMsgClientId() {
        return msgClientId;
    }

    public void setMsgClientId(String msgClientId) {
        this.msgClientId = msgClientId;
    }

    public String getMsgServerId() {
        return msgServerId;
    }

    public void setMsgServerId(String msgServerId) {
        this.msgServerId = msgServerId;
    }

    public String getCustomApnsText() {
        return customApnsText;
    }

    public void setCustomApnsText(String customApnsText) {
        this.customApnsText = customApnsText;
    }

    public String gettMembers() {
        return tMembers;
    }

    public void settMembers(String tMembers) {
        this.tMembers = tMembers;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
