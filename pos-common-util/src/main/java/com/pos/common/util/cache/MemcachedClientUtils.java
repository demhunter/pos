/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.cache;

import com.pos.common.util.validation.Preconditions;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 缓存客户端生成工具
 *
 * author chi.chen
 * date 16-3-1
 * time 下午12:03
 */
public class MemcachedClientUtils implements InitializingBean, DisposableBean {

    private final static Logger LOG = LoggerFactory.getLogger(MemcachedClientUtils.class);

    private MemcachedClient memcachedClient;

    private MemcachedClient buildMemcachedClient(String addr) {
        MemcachedClientBuilder clientBuilder = new XMemcachedClientBuilder(AddrUtil.getAddresses(addr));

        clientBuilder.setFailureMode(false);
        clientBuilder.setConnectionPoolSize(3);
        clientBuilder.setSessionLocator(new KetamaMemcachedSessionLocator());

        try {
            MemcachedClient client = clientBuilder.build();
            client.setSanitizeKeys(true);
            int compressionLimitBytes = 1024;
            client.getTranscoder().setCompressionThreshold(compressionLimitBytes);

            return client;
        } catch (IOException e) {
            LOG.error("memcache初始化失败，addr={}", addr, e);
            e.printStackTrace();
        }

        return null;
    }

    public void setCacheValue(String key, int exp, Object value) {
        Preconditions.checkArgument(key != null && value != null && exp >= 0, "Cache设置参数不正确！");

        try {
            memcachedClient.set(key, exp, value);
        } catch (Exception e) {
            LOG.error("设置Cache异常，key={}，exp={}，value={}", key, exp, value, e);
            throw new IllegalStateException("设置Cache异常");
        }
    }

    @SuppressWarnings("all")
    public Object getCacheValue(String key) {
        Preconditions.checkNotNull(key, "Cache key不能为空！");

        try {
            Object value = memcachedClient.get(key);
            memcachedClient.delete(key);
            return value;
        } catch (Exception e) {
            LOG.error("获取Cache异常，key={}", key, e);
            throw new IllegalStateException("获取Cache异常");
        }
    }

    public Object getCacheNoDeletion(String key) {
        Preconditions.checkNotNull(key, "Cache key不能为空！");

        try {
            return memcachedClient.get(key);
        } catch (Exception e) {
            LOG.error("获取Cache异常，key={}", key, e);
            throw new IllegalStateException("获取Cache异常");
        }
    }

    public MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }

    public void setMemcachedClient(String addr) {
        this.memcachedClient = buildMemcachedClient(addr);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(memcachedClient, "memcachedClient不能为空！");
    }

    @Override
    public void destroy() throws Exception {
        this.memcachedClient.shutdown();
    }

}
