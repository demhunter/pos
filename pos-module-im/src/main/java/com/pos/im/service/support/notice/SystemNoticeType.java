/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.notice;

import com.pos.common.util.exception.IllegalParamException;

/**
 * 自定义系统通知类型枚举
 *
 * @author wangbing
 * @version 1.0, 2017/9/13
 */
public enum SystemNoticeType {

    INTERNAL_MESSAGE_NOTICE((byte) 1, "站内信消息通知"),

    TIPS_MESSAGE_NOTICE((byte) 2, "小灰条消息通知");

    private Byte code;

    private String desc;

    SystemNoticeType(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SystemNoticeType getEnum(byte code) {
        for (SystemNoticeType type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的自定义系统通知类型code值");
    }

    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
