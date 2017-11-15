/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import java.util.Random;

/**
 * 一个短序列号生成工具.
 * <p>
 * 根据指定的UID生成一个6位且不重复的短序列号，由数字和大小写字母组成.</br>
 * 实现原理是自定义一个32进制字符，计算并转换UID的每一位原始值，如果转换后不足6位时再随机补全.</br>
 * 因此，UID最大只支持32的6次方: 0 ~ 1073741824，且因为补全的存在，不保证同一个UID多次生成的结果相同！
 *
 * @author wayne
 * @version 1.0, 2017/1/4
 */
public class ShortSerialNum {

    /**
     * 自定义进制（去掉0,1,o,l,I等易混淆的字母/数字，形成32位自定义进制转码和24位补全转码）
     */
    private static final char[] r = new char[] { 'Q', 'w', 'E', '8', 'a', 'S', '2', 'd', 'Z', 'x', '9', 'c',
            '7', 'p', '5', 'i', 'K', '3', 'm', 'j', 'U', 'f', 'r', '4', 'V', 'y', 't', 'N', '6', 'b', 'g', 'H' };

    /**
     * 自动补全组（不能与自定义进制重复）
     */
    private static final char[] b = new char[] { 'q', 'W', 'e', 'A', 's', 'D', 'z', 'X',
            'C', 'P', 'L', 'k', 'M', 'J', 'u', 'F', 'R', 'v', 'Y', 'T', 'n', 'B', 'G', 'h' };

    /**
     * 进制长度
     */
    private static final int l = r.length;

    /**
     * 序列长度
     */
    private static final int serialLength = 6;

    /**
     * UID最大值
     */
    private static final long limit = 1073741824;

    public static String build(long uid) {
        if (uid < 0 || uid > limit) {
            throw new IllegalArgumentException(
                    "生成短序列号失败！cause: uid值范围溢出(" + uid + "), 有效值范围0~" + limit);
        }
        char[] buf = new char[32];
        int charPos = 32;

        while ((uid / l) > 0) {
            buf[--charPos] = r[(int) (uid % l)];
            uid /= l;
        }
        buf[--charPos] = r[(int) (uid % l)];
        String s = new String(buf, charPos, (32 - charPos));

        // 长度不足的自动随机补全
        if (s.length() < serialLength) {
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < serialLength - s.length(); i++) {
                sb.append(b[random.nextInt(24)]);
            }
            s += sb.toString();
        }
        return s;
    }

}