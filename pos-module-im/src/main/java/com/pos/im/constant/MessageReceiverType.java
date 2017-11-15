/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.constant;

/**
 * 消息接收方类型
 *
 * @author wangbing
 * @version 1.0, 2017/9/13
 */
public enum MessageReceiverType {

    PERSON(0, "个人"),

    GROUP(1, "群组");

    private int code;

    private String desc;

    MessageReceiverType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
