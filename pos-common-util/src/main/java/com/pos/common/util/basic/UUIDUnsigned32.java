/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import java.util.UUID;

/**
 * 一个无符号(`-`)的32位UUID生成工具.
 *
 * @author wayne
 * @version 1.0, 2016/7/11
 */
public class UUIDUnsigned32 {

    private UUIDUnsigned32() {
    }

    /**
     * 随机生成一个无符号的32位UUID字符串.
     */
    public static String randomUUIDString() {
        UUID uuid = UUID.randomUUID();
        return (digits(uuid.getMostSignificantBits() >> 32, 8)
                + digits(uuid.getMostSignificantBits() >> 16, 4)
                + digits(uuid.getMostSignificantBits(), 4)
                + digits(uuid.getLeastSignificantBits() >> 48, 4)
                + digits(uuid.getLeastSignificantBits(), 12));
    }

    /**
     * 将无符号的32位UUID字符串转换为有符号的UUID对象.
     *
     * @param uuidStr 指定的无符号的32位UUID字符串
     * @throws IllegalArgumentException uuidStr为空或者长度不等于32
     */
    public static UUID fromUnsignedString(String uuidStr) {
        if (uuidStr == null || uuidStr.length() != 32) {
            throw new IllegalArgumentException("Invalid UUID string: " + uuidStr);
        }

        long mostSigBits = Long.decode("0x" + uuidStr.substring(0, 8));
        mostSigBits <<= 16;
        mostSigBits |= Long.decode("0x" + uuidStr.substring(8, 12));
        mostSigBits <<= 16;
        mostSigBits |= Long.decode("0x" + uuidStr.substring(12, 16));

        long leastSigBits = Long.decode("0x" + uuidStr.substring(16, 20));
        leastSigBits <<= 48;
        leastSigBits |= Long.decode("0x" + uuidStr.substring(20));

        return new UUID(mostSigBits, leastSigBits);
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    public static void main(String[] args) {
        String uuid = UUIDUnsigned32.randomUUIDString();
        System.out.println("Get unsigned UUID：" + uuid);
        System.out.println("Parse signed UUID：" + UUIDUnsigned32.fromUnsignedString(uuid));
    }

}