/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.domain;

import com.pos.im.constant.InternalMessageType;

import java.io.Serializable;
import java.util.Date;

/**
 * IM站内消息的领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/11/4
 */
public class InternalMessage implements Serializable {

    private static final long serialVersionUID = 102325321489010724L;

    private Long id;

    /**
     * 消息类型
     *
     * @see InternalMessageType
     */
    private byte type;

    /**
     * 消息子类型
     *
     * @see InternalMessageType.SubType
     */
    private Integer subType;

    private String title; // 消息标题

    private String content; // 消息内容

    private String extendInfo; // 消息扩展信息

    private Long targetId; // 消息关联的目标ID，比如type=案例会话，则targetId表示案例会话ID

    private Date sendTime; // 消息发送时间

    private String parameters; // v1.6.0 * 参数列表，以英文逗号分隔多个参数，注意参数顺序（使用注意事项：要使用此字段，必须把tartgetId置为0）

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

}