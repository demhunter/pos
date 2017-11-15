/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

/**
 * 用户加入会话的身份定义.
 *
 * @author wayne
 * @version 1.0, 2016/7/12
 */
public enum UserJoinSessionType implements CommonByteEnum {

    /**
     * 会话发起者
     */
    CREATOR((byte) 1),

    /**
     * 会话跟踪者(客服)
     */
    SERVANT((byte) 2),

    /**
     * 其他参与者
     */
    MEMBERS((byte) 3);

    private final byte code;

    UserJoinSessionType(final byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public boolean compare(byte code) {
        return this.code == code;
    }

    public static UserJoinSessionType getEnum(byte code) {
        for (UserJoinSessionType type : UserJoinSessionType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

}
