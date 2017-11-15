/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 自定义普通消息类型枚举
 *
 * @author wangbing
 * @version 1.0, 2017/9/11
 */
public enum CustomizedMessageType implements CommonByteEnum {

    MSG_TIPS((byte) 1, "小灰条消息");

    CustomizedMessageType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public CustomizedMessageType getEnum(byte code) {
        for (CustomizedMessageType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalParamException("非法的自定义普通消息类型code值");
    }

    private byte code;

    private String desc;

    public String getDesc() {
        return desc;
    }

    @Override
    public byte getCode() {
        return code;
    }
}
