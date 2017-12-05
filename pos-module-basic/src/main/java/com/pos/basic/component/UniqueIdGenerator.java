/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.component;

import com.pos.basic.constant.RedisConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 12位（千亿级）唯一id产生器
 *
 * @author cc
 * @version 1.0, 2017/3/17
 */
public class UniqueIdGenerator {

    /*private final static Logger logger = LoggerFactory.getLogger(UniqueIdGenerator.class);

    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    @Value("${unique_id_generator_num_pre}")
    private String uniqueIdGeneratorNumPre; // 唯一id数字前缀

    @Value("${unique_id_generator_num_limit}")
    private String uniqueIdGeneratorNumLimit; // id最大数值限制

    public void initialization() {
        Serializable value = redisTemplate.opsForValue().get(RedisConstants.UNIQUE_ID_TWELVE);

        if (value == null) {
            String initialNum = RandomStringUtils.randomNumeric(11);
            String numberStr = uniqueIdGeneratorNumPre + initialNum;
            redisTemplate.opsForValue().set(RedisConstants.UNIQUE_ID_TWELVE, numberStr);
        } else {
            // v1.7.0 * 区分不同环境的唯一id增长的数字前缀，替换旧的唯一id前缀
            Long oldNumber = Long.valueOf((String) value);
            Long oldPreNum = oldNumber/100000000000L; // 获取旧id前缀数字
            Long preNum = Long.valueOf(uniqueIdGeneratorNumPre);
            logger.info("校验是否需要替换旧的唯一id前缀，旧id前缀数字为{" + oldPreNum + "}");
            if (oldPreNum < preNum) {
                logger.info("替换旧的唯一id前缀，新唯一id前缀数字为{" + preNum + "}");
                String initialNum = RandomStringUtils.randomNumeric(11);
                String numberStr = uniqueIdGeneratorNumPre + initialNum;
                redisTemplate.opsForValue().set(RedisConstants.UNIQUE_ID_TWELVE, numberStr);
            }
        }
    }

    public String generateTwelveUniqueId() {
        Long number = redisTemplate.opsForValue().increment(RedisConstants.UNIQUE_ID_TWELVE, 1);

        Long numberLimit = Long.valueOf(uniqueIdGeneratorNumLimit);
        if (number > numberLimit) {
            throw new IllegalStateException("唯一id超过限制{" + uniqueIdGeneratorNumLimit + "}");
        }

        return String.valueOf(number);
    }*/
}
