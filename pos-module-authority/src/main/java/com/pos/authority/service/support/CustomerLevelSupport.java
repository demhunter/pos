/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.support;

import com.pos.authority.dao.CustomerLevelConfigDao;
import com.pos.authority.domain.CustomerLevelConfig;
import com.pos.basic.constant.RedisConstants;
import com.pos.common.util.exception.IllegalParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 客户等级支持类
 *
 * @author wangbing
 * @version 1.0, 2017/12/4
 */
@Component
public class CustomerLevelSupport {

    private final static Logger LOG = LoggerFactory.getLogger(CustomerLevelSupport.class);

    @Resource
    private CustomerLevelConfigDao customerLevelConfigDao;

    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    @PostConstruct
    private void initialize() {
        LOG.info("开始加载客户等级配置");
        List<CustomerLevelConfig> configs = customerLevelConfigDao.getConfigs();
        if (CollectionUtils.isEmpty(configs)) {
            throw new RuntimeException("客户等级配置加载失败：无可用的客户等级配置");
        }
        LOG.info("当前共有{}种客户等级配置", configs.size());
        for (CustomerLevelConfig config : configs) {
            // 把等级加入到已配置等级集合中
            redisTemplate.opsForSet().add(RedisConstants.POS_CUSTOMER_LEVELS, config.getLevel());
            // 保存当前用户等级相应的配置信息
            redisTemplate.opsForValue().set(RedisConstants.POS_CUSTOMER_LEVEL_CONFIG + config.getLevel(), config);
        }
        LOG.info("客户等级配置加载完毕");
    }

    /**
     * 获取指定等级的配置信息
     *
     * @param level 等级数
     * @return 相应等级的配置信息
     * @throws IllegalParamException 无当前等级时
     */
    public CustomerLevelConfig getLevelConfig(int level) throws IllegalParamException {
        CustomerLevelConfig config = (CustomerLevelConfig) redisTemplate.opsForValue().get(RedisConstants.POS_CUSTOMER_LEVEL_CONFIG + level);

        if (config == null) {
            // 缓存中没有相应配置，从数据库查找相应配置
            config = customerLevelConfigDao.getLevelConfig(level);
            if (config == null) {
                LOG.error("系统无Lv" + level + "等级的配置信息！");
                throw new IllegalParamException("系统无Lv" + level + "等级的配置信息！");
            }
            // 把等级加入到已配置等级集合中
            redisTemplate.opsForSet().add(RedisConstants.POS_CUSTOMER_LEVELS, config.getLevel());
            // 保存当前用户等级相应的配置信息
            redisTemplate.opsForValue().set(RedisConstants.POS_CUSTOMER_LEVEL_CONFIG + config.getLevel(), config);
        }

        return config;
    }

    /**
     * 获取当前系统中最低等级的配置信息
     *
     * @return 配置信息
     */
    public CustomerLevelConfig getMinLevelConfig() {
        Set<Integer> levelSet = getLevels();

        Integer minLevel = 100;
        for (Integer level : levelSet) {
            if (level.compareTo(minLevel) < 0) {
                minLevel = level;
            }
        }

        return getLevelConfig(minLevel);
    }

    /**
     * 获取当前系统中最低等级的配置信息
     *
     * @return 配置信息
     */
    public CustomerLevelConfig getMaxLevelConfig() {
        Set<Integer> levelSet = getLevels();

        Integer maxLevel = 1;
        for (Integer level : levelSet) {
            if (level.compareTo(maxLevel) > 0) {
                maxLevel = level;
            }
        }

        return getLevelConfig(maxLevel);
    }

    /**
     * 获取当前系统配置所有客户等级
     *
     * @return 配置客户等级集合
     */
    public Set<Integer> getLevels() {
        Set<Serializable> set = redisTemplate.opsForSet().members(RedisConstants.POS_CUSTOMER_LEVELS);
        if (CollectionUtils.isEmpty(set)) {
            LOG.error("系统没有配置客户等级");
            throw new IllegalStateException("系统没有配置客户等级!");
        }

        return set.stream().map(e -> (Integer) e).collect(Collectors.toSet());
    }

}
