/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.date;

/**
 * 时间计数器，用于统计某段程序的执行时间(ms)，该类非常简单，使用该类的目的只有一个：让代码更整洁.
 *
 * @author wayne
 * @version 1.0, 2017/4/2
 */
public class TimeCounter {

    private long time;

    public TimeCounter start() {
        time = System.currentTimeMillis();
        return this;
    }

    public long end() {
        return (System.currentTimeMillis() - time) / 1000L;
    }

}