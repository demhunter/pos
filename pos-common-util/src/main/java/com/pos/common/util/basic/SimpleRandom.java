/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import java.math.BigDecimal;
import java.util.Random;

/**
 * 一个简单的伪随机数生成器.
 * <p>
 * 基于java.util.Random, 提供它所不具备的一些按条件生成随机数的方法.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public class SimpleRandom {

    private final Random random;

    /**
     * 创建一个新的随机数生成器.
     */
    public SimpleRandom() {
        random = new Random();
    }

    /**
     * 使用单个long种子创建一个新随机数生成器.
     *
     * @param seed 初始种子
     */
    public SimpleRandom(long seed) {
        random = new Random(seed);
    }

    /**
     * 返回java.util.Random实例.
     *
     * @return
     */
    public Random getRandom() {
        return random;
    }

    /**
     * 随机生成一个在指定范围内的整数.
     *
     * @param min 随机数生成范围的最小值
     * @param max 随机数生成范围的最大值
     * @return
     * @throws IllegalArgumentException min小于max
     */
    public int next(int min, int max) {
        if (max > min) {
            return random.nextInt(max - min + 1) + min;
        } else if (min == max) {
            return min;
        } else {
            throw new IllegalArgumentException("max必须大于等于min! min = " + min + "; max = " + max);
        }
    }

    /**
     * 随机生成一组在指定范围内的整数, 且保证不会重复(生成范围较大时不推荐使用).
     *
     * @param min 随机数生成范围的最小值
     * @param max 随机数生成范围的最大值
     * @param num 随机数生成个数, 必须在生成范围之内(即num不能大于max-min+1)
     * @return
     * @throws IllegalArgumentException min小于max, 或者num大于(max-min+1)
     */
    public int[] getArrays(int min, int max, int num) {
        if (max < min) {
            throw new IllegalArgumentException("max必须大于等于min! min = " + min + "; max = " + max);
        }

        int amount = max - min + 1;
        if (num > amount) {
            throw new IllegalArgumentException("num必须小于max-min! num = " + num + "; max-min+1 = " + amount);
        }

        if (amount <= 1) {
            return new int[] { next(min, max) };
        }

        // 生成一组不重复的随机种子
        int[] seed = new int[amount];
        for (int i = 0, j = min; j <= max; i++, j++) {
            seed[i] = j;
        }

        int[] result = new int[num];
        for (int i = 0; i < num; i++) {
            int index = random.nextInt(seed.length - i);
            result[i] = seed[index];
            seed[index] = seed[seed.length - 1 - i];
        }

        return result;
    }

    /**
     * 根据既定几率, 计算是否触发某事件（最大支持亿分之一）.
     *
     * @param rate 几率值, 等于0时, 始终返回false; 大于等于100时, 始终返回true; 其余情况按几率计算:<br>
     *              1 ~ 99: 表示按1% ~ 99%的几率触发, 可以带小数点<br>
     *              小于0时, 小数点后面每多1位有效数, 几率以100 * 10的倍数计算, 如:<br>
     *              0.1 = 千分之1; 0.01 = 万分之1; 0.11 = 万分之11; 1.11 = 万分之111; 11.11 = 万分之1111...
     * @return true表示触发, 反之未触发.
     */
    public boolean probability(BigDecimal rate) {
        if (rate.compareTo(BigDecimal.ZERO) == 0
                || rate.compareTo(new BigDecimal(0.000001)) == -1) {
            return false;
        }
        if (rate.compareTo(new BigDecimal(100)) >= 0) {
            return true;
        }

        // 获取几率小数点位数
        int len = MathUtils.getDecimalPlaces(rate);
        int value, limit;
        if (len > 0) {
            value = (int) (rate.doubleValue() * (int) Math.pow(10, len));
            limit = 100 * (int) Math.pow(10, len);
        } else {
            // 没有小数位数时, 直接按百分比计算
            value = rate.intValue();
            limit = 100;
        }

        return next(1, limit) <= value;
    }

}