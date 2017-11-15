/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import java.math.BigDecimal;

/**
 * 数学运算工具类.
 * <p>
 * 提供一些java.lang.Math未提供的数学函数.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public class MathUtils {

    private MathUtils() {
    }

    /**
     * 精确的加法运算.
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确的减法运算.
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确的乘法运算.
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 相对精确的除法运算.
     * <p>
     * 当发生除不尽的情况时, 精确到小数点以后10位, 以后的数字四舍五入.
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, 10);
    }

    /**
     * 相对精确的除法运算.
     * <p>
     * 当发生除不尽的情况时, 由scale参数指定精度, 以后的数字四舍五入.
     *
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示需要精确到小数点以后几位
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero.");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确的小数位四舍五入处理.
     *
     * @param val 待四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double val, int scale) {
        BigDecimal number = new BigDecimal(Double.toString(val));
        return getRound(number,scale).doubleValue();
    }

    /**
     * 比较指定的两个浮点数.
     *
     * @param a 待比较的数1
     * @param b 待比较的数2
     * @return -1: a小于b; 0: 二者相等; 1: a大于b.
     */
    public static int compare(double a, double b) {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.compareTo(b2);
    }

    /**
     * 获取BigDecimal的字符串表示.
     * <p>
     * 小数点后尾数无意义的0将被剔除, 比如1.10将返回1.1, 0.00返回0.
     *
     * @param val 指定的BigDecimal
     * @return
     */
    public static String getDecimalString(BigDecimal val) {
        if (val != null) {
            if (val.compareTo(BigDecimal.ZERO) == 0) {
                return "0";
            } else {
                return val.stripTrailingZeros().toPlainString();
            }
        } else {
            return "";
        }
    }

    /**
     * 获取BigDecimal的小数点位数.
     * <p>
     * 小数点后尾数无意义的0不包含在内, 比如1.10其小数点位数返回1, 又如1.0其小数点位数返回0.
     *
     * @param val 指定的BigDecimal
     * @return 小数点后的位数
     */
    public static int getDecimalPlaces(BigDecimal val) {
        if (val != null && val.compareTo(BigDecimal.ZERO) != 0) {
            String str = val.toString();
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '.') {
                    int count = str.length() - (i + 1);
                    for (int j = str.length() - 1; j > i; j--) {
                        if (str.charAt(j) == '0') {
                            count--;
                        } else {
                            break;
                        }
                    }

                    return count;
                }
            }
        }

        return 0;
    }

    /**
     * 计算指定值占总值的百分比.
     *
     * @param val 指定值
     * @param total 总值
     * @param scale 小数点后保留几位
     * @return
     */
    public static BigDecimal getPercentage(BigDecimal val, BigDecimal total, int scale) {
        if (total.compareTo(val) == 1) {
            return val.divide(total, scale, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 计算指定的数字，保留的小数位由scale决定
     * @param val
     * @param scale
     * @return
     */
    public static BigDecimal getRound(BigDecimal val,int scale){
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero.");
        }
        BigDecimal one = new BigDecimal("1");
        return val.divide(one, scale, BigDecimal.ROUND_HALF_UP);
    }

}