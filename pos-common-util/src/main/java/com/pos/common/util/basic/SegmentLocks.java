/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个简单的ReentrantLock分段锁实现.
 * <p>
 * 简单的根据指定的UID对锁总数取模来获得一个锁对象, 没有考虑数据大小和锁空闲. 注意, 在分布式环境下不能使用该分段锁! </br>
 * 因为根据哈希一致性算法, 同一个用户的请求可能会被转发到不同的服务器, 导致该用户拿到的不是同一把锁!
 *
 * @author wayne
 * @version 1.0, 2016/7/25
 */
public class SegmentLocks {

    private final ReentrantLock[] locks;

    public SegmentLocks() {
        locks = new ReentrantLock[16];
        initLocks(false);
    }

    public SegmentLocks(int lockTotal, boolean fair) {
        locks = new ReentrantLock[lockTotal];
        initLocks(fair);
    }

    private void initLocks(boolean fair) {
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock(fair);
        }
    }

    public ReentrantLock getLock(Long uid) {
        return locks[(int) (uid % locks.length)];
    }

}