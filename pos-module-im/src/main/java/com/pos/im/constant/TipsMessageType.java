/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 小灰条消息类型
 *
 * @author wangbing
 * @version 1.0, 2017/9/11
 */
public enum TipsMessageType implements CommonByteEnum {

    DEFAULT((byte) 1, "普通文本小灰条提示消息");

    private byte code;

    private String desc;

    TipsMessageType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 获取code值对应的枚举类型
    public TipsMessageType getEnum(byte code) {
        for (TipsMessageType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的小灰条消息类型code值");
    }

    // 自定义消息子类型对应自定义消息类型
    public CustomizedMessageType getCustomizedMessageType() {
        return CustomizedMessageType.MSG_TIPS;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public byte getCode() {
        return code;
    }
}
