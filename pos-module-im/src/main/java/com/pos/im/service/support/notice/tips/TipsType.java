/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.notice.tips;

import com.pos.common.util.exception.IllegalParamException;

/**
 * 小灰条消息类型枚举
 *
 * @author wangbing
 * @version 1.0, 2017/9/13
 */
public enum TipsType {

    DEFAULT((byte) 0, "put文本小灰条消息");

    private byte code;

    private String desc;

    TipsType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TipsType getEnum(byte code) {
        for (TipsType type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的小灰条消息类型枚举code值");
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
