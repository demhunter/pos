/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

/**
 * 会话关闭的原因定义.
 *
 * @author wayne
 * @version 1.0, 2016/10/19
 */
public enum SessionClosedReason implements CommonByteEnum {

    /**
     * 系统清理会话
     */
    USER_CLOSED((byte) 1),

    /**
     * 客服关闭会话
     */
    SERVANT_CLOSED((byte) 2),

    /**
     * 清理临时会话
     */
    CLEAR_TEMP_SESSION((byte) 3),

    /**
     * 清理不活跃的会话
     */
    CLEAR_INACTIVE_SESSION((byte) 4);

    private final byte code;

    SessionClosedReason(final byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public boolean compare(byte code) {
        return this.code == code;
    }

    public static SessionClosedReason getEnum(byte code) {
        for (SessionClosedReason type : SessionClosedReason.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

}
