/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

/**
 * 用户Ticket类型定义.
 *
 * @author wayne
 * @version 1.0, 2016/11/8
 */
public enum UserTicket implements CommonByteEnum {

    /**
     * 活动优惠券
     */
    ACTIVITY_COUPON((byte) 1, "at_cp:", 60 * 5),
    /**
     * 预约订单
     */
    BOOKING((byte) 2, "booking:", 60 * 10);

    private final byte code;

    private final String prefix; // Token缓存的前缀标识符

    private final int expiration; // Token的过期时间（秒）

    UserTicket(final byte code, final String prefix, final int expiration) {
        this.code = code;
        this.prefix = prefix;
        this.expiration = expiration;
    }

    public byte getCode() {
        return code;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getExpiration() {
        return expiration;
    }

    public static UserTicket getEnum(byte code) {
        for (UserTicket type : UserTicket.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

}