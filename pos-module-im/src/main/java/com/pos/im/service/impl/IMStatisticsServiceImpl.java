/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.impl;

import com.google.common.collect.Lists;
import com.pos.basic.constant.RedisConstants;
import com.pos.im.dao.StatisticsDao;
import com.pos.im.data.IMCompanyStatisticsData;
import com.pos.im.service.IMStatisticsService;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * IM统计实现（包含查库和查Redis）
 *
 * @author cc
 * @version 1.0, 16/7/18
 */
@Service
public class IMStatisticsServiceImpl implements IMStatisticsService {

    Logger logger = LoggerFactory.getLogger(IMStatisticsService.class);

    @Resource
    StatisticsDao statisticsDao;

    @Resource
    RedisTemplate<Serializable, Serializable> redisTemplate;

    @Override
    public List<Map<String, Object>> queryCompanyChatCount(List<Long> companyIds) {
        if (CollectionUtils.isEmpty(companyIds)) {
            return Lists.newArrayList();
        }
        return statisticsDao.queryCompanyChatCount(companyIds);
    }

    @Override
    public List<Map<String, Object>> queryCaseCurrentChatCount(List<Long> caseIds, Long companyId) {
        if (CollectionUtils.isEmpty(caseIds)) {
            return Lists.newArrayList();
        }

        return statisticsDao.queryCaseCurrentChatCount(caseIds, companyId);
    }

    @Override
    public List<Map<String, Object>> queryCaseContactAndChatTotal(List<Long> caseIds, Long companyId) {
        if (CollectionUtils.isEmpty(caseIds)) {
            return Lists.newArrayList();
        }

        return statisticsDao.queryCaseContactAndChatTotal(caseIds, companyId);
    }

    @Override
    public List<Map<String, Object>> queryEmployeeContactAndChatTotalByCase(List<Long> caseIds, Long userId) {
        if (CollectionUtils.isEmpty(caseIds) || userId == null) {
            return Lists.newArrayList();
        }

        return statisticsDao.queryEmployeeContactAndChatTotalByCase(caseIds, userId);
    }

    @Override
    public List<Map<String, Object>> queryUserContactAndChatTotal(
            List<Long> userIds, String userType, Long companyId) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Lists.newArrayList();
        }

        return statisticsDao.queryUserContactAndChatTotal(userIds, userType, companyId);
    }

    @Override
    public List<Map<String, Object>> queryUserCurrentChatCount(
            List<Long> userIds, String userType, Long companyId) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Lists.newArrayList();
        }

        return statisticsDao.queryUserCurrentChatCount(userIds, userType, companyId);
    }

    @Override
    public void incrementCompaniesIMSession(List<Long> companyIds) {
        if (CollectionUtils.isEmpty(companyIds)) {
            logger.error("IMStatisticsServiceImpl companyId不能为空！");
            return;
        }

        for (Long companyId : companyIds) {
            handleCompanyIMSessionUpdate(companyId);
        }
    }

    @Override
    public IMCompanyStatisticsData queryIMCompanyStatisticsData(Long companyId) {
        IMCompanyStatisticsData statisticsData = new IMCompanyStatisticsData();

        // 今日新增聊天数key
        String imSessionTodayKey = RedisConstants.COM_IM_SESSION_PD_KEY +
                String.valueOf(companyId) + RedisConstants.SEP + new LocalDate().toString("yyyyMMdd");
        // 累计聊天总数key
        String imSessionTotalKey = RedisConstants.COM_IM_SESSION_TOTAL_KEY + String.valueOf(companyId);

        statisticsData.setSessionCountToday(convertStatisticsData(imSessionTodayKey));
        statisticsData.setSessionCountTotal(convertStatisticsData(imSessionTotalKey));

        return statisticsData;
    }

    @Override
    public int querySessionCount(Boolean available) {
        return statisticsDao.querySessionCount(available);
    }

    private void handleCompanyIMSessionUpdate(Long companyId) {
        // 今日新增聊天数key
        String imSessionTodayKey = RedisConstants.COM_IM_SESSION_PD_KEY +
                String.valueOf(companyId) + RedisConstants.SEP + new LocalDate().toString("yyyyMMdd");
        // 累计聊天总数key
        String imSessionTotalKey = RedisConstants.COM_IM_SESSION_TOTAL_KEY + String.valueOf(companyId);

        redisTemplate.opsForValue().increment(imSessionTodayKey, 1L);
        redisTemplate.opsForValue().increment(imSessionTotalKey, 1L);
    }

    private Long convertStatisticsData(String key) {
        String valueStr = (String) redisTemplate.opsForValue().get(key);
        return valueStr == null ? 0L : Long.valueOf(valueStr);
    }
}
