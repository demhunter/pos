/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单的字符串工具类.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public class SimpleStringUtils {

    private SimpleStringUtils() {
    }

    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 获取一个指定长度的随机字符串.
     *
     * @param length 随机字符串的长度
     * @return 由0~9的数字以及26个大写英文字母所组成的随机字符串
     * @throws IllegalArgumentException 如果length小于1
     */
    public static String getRandomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length必须大于0! length: " + length);
        }

        StringBuilder result = new StringBuilder(length);
        for (int i = 0, j; i < length; i++) {
            j = ((Double) (Math.random() * 997)).intValue() % DIGITS.length;
            result.append(String.valueOf(DIGITS[j]));
        }

        return result.toString();
    }

    /**
     * 判断字符串是否由中文组成.
     *
     * @param source 指定的字符串
     * @return 如果字符串的所有内容都是中文, 则返回true, 否则返回false.
     */
    public static boolean isChinese(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return false;
        }

        char[] ch = source.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char temp = ch[i];
            if (!isChinese(temp)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否由数字组成.
     *
     * @param source 指定的字符串
     * @return 如果字符串的所有内容都是数字, 则返回true, 否则返回false.
     */
    public static boolean isNumber(String source) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(source);
        return m.matches();
    }

    /**
     * 判断字符串是否由字母组成(包括大小写: a~z, A~Z).
     *
     * @param source 指定的字符串
     * @return 如果字符串的所有内容都是字母, 则返回true, 否则返回false.
     */
    public static boolean isLetter(String source) {
        Pattern p = Pattern.compile("^[a-zA-Z]+$");
        Matcher m = p.matcher(source);
        return m.matches();
    }

    /**
     * 判断字符串所包含的内容是否由数字, 或者字母(包括大小写: a~z, A~Z)组成.
     *
     * @param source     指定的字符串
     * @return 如果所有内容都是数字或者字母, 则返回true, 否则返回false.
     */
    public static boolean isNumberOrLetter(String source) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher m = p.matcher(source);
        return m.matches();
    }

    /**
     * 判断字符串所包含的内容是否由数字，以及字母(包括大小写: a~z, A~Z)组成.
     *
     * @param source     指定的字符串
     * @return 如果所有内容都是数字和字母(必须同时包含数字和字母), 则返回true, 否则返回false.
     */
    public static boolean isNumberAndLetter(String source) {
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]+$");
        Matcher m = p.matcher(source);
        return m.matches();
    }

    /**
     * 将字符串解析为int类型.
     *
     * @param source     指定的字符串
     * @param defaultVal 默认值, 如果转换失败, 返回该默认值
     * @return 转换后的数字
     */
    public static int parseInt(String source, int defaultVal) {
        if (Strings.isNullOrEmpty(source)) {
            return defaultVal;
        }

        int result;
        try {
            result = Integer.parseInt(source);
        } catch (NumberFormatException e) {
            result = defaultVal;
        }

        return result;
    }

    /**
     * 将字符串解析为long类型.
     *
     * @param source     指定的字符串
     * @param defaultVal 默认值, 如果转换失败, 返回该默认值
     * @return 转换后的数字
     */
    public static long parseLong(String source, long defaultVal) {
        if (Strings.isNullOrEmpty(source)) {
            return defaultVal;
        }

        long result;
        try {
            result = Long.parseLong(source);
        } catch (NumberFormatException e) {
            result = defaultVal;
        }

        return result;
    }

    /**
     * 将字符串解析为float类型.
     *
     * @param source     指定的字符串
     * @param defaultVal 默认值, 如果转换失败, 返回该默认值
     * @return 转换后的数字
     */
    public static float parseFloat(String source, float defaultVal) {
        if (Strings.isNullOrEmpty(source)) {
            return defaultVal;
        }

        float result;
        try {
            result = Float.parseFloat(source);
        } catch (NumberFormatException e) {
            result = defaultVal;
        }

        return result;
    }

    /**
     * 将字符串解析为double类型.
     *
     * @param source     指定的字符串
     * @param defaultVal 默认值, 如果转换失败, 返回该默认值
     * @return 转换后的数字
     */
    public static double parseDouble(String source, double defaultVal) {
        if (Strings.isNullOrEmpty(source)) {
            return defaultVal;
        }

        double result;
        try {
            result = Double.parseDouble(source);
        } catch (NumberFormatException e) {
            result = defaultVal;
        }

        return result;
    }

    /**
     * 检测是否有emoji(苹果表情)字符.
     *
     * @param source 原字符串
     * @return 查询结果
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();

        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i+1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b || hs == 0x2b50|| hs == 0x231a ) {
                    return true;
                }
                if (source.length() > 1 && i < source.length() - 1) {
                    char ls = source.charAt(i+1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static <T> String joinListToStr(List<T> elements, String joiner) {
        return Joiner.on(joiner).skipNulls().join(elements);
    }

    public static List<String> splitJoinedStr(String str, String separator) {
        Iterable<String> splitStr = Splitter.on(separator).trimResults().omitEmptyStrings().split(str);
        return Lists.newArrayList(splitStr);
    }

    private static boolean isChinese(char character) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(character);
        return  (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
    }
}