/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

/**
 * 货币工具类
 *
 * @author cc
 * @version 1.0, 16/9/13
 */
public class CurrencyUtils {

    /**
     * 元到万元转换
     *
     * @param price 元
     * @return 万元（String）
     */
    public static String toTenThousandStr(Float price) {
        return String.format("%.2f", price / 10000);
    }

    /**
     * 元到万元转换
     *
     * @param price 元
     * @return 万元
     */
    public static Float toTenThousand(Float price) {
        return Float.valueOf(toTenThousandStr(price));
    }
}
