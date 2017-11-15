/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

/**
 * IM历史消息的处理状态定义.
 *
 * @author wayne
 * @version 1.0, 2016/10/21
 */
public enum HistoryMessageStatus implements CommonByteEnum {

    /**
     * 该时段无历史消息的占位符
     */
    UNAVAILABLE((byte) -1),

    /**
     * 尚未入库
     */
    UNSAVED((byte) 0),

    /**
     * 已经入库
     */
    PERSISTED((byte) 1),

    /**
     * 等待重试
     */
    WAIT_RETRY((byte) 2),

    /**
     * 入库失败
     */
    FAILED((byte) 3);

    private final byte code;

    HistoryMessageStatus(final byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public boolean compare(byte code) {
        return this.code == code;
    }

    public static HistoryMessageStatus getEnum(byte code) {
        for (HistoryMessageStatus type : HistoryMessageStatus.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

}
