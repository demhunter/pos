/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.sms.domain;

import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信领域对象.
 *
 * @author wayne
 * @version 1.0, 2016/6/3
 */
public class Sms implements Serializable {

    private static final long serialVersionUID = -6796094576120474049L;

    private Long id;

    private byte type; // 短信类型

    private String phone; // 接收人手机号

    private String content; // 短信内容

    private short status; // 发送状态

    private Date sendTime; // 发送时间

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}