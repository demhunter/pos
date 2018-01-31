/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.impl;

import com.google.common.collect.Lists;
import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.service.support.CustomerLevelSupport;
import com.pos.basic.dto.CommonEnumDto;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.transaction.dao.PosStatisticsDao;
import com.pos.transaction.dto.statistics.PosGeneralStatisticsDto;
import com.pos.transaction.dto.statistics.TransactionDailyStatisticsDto;
import com.pos.transaction.service.PosStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 交易统计ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/12/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosStatisticsServiceImpl implements PosStatisticsService {

    private static final Logger LOG = LoggerFactory.getLogger(PosStatisticsServiceImpl.class);

    @Resource
    private PosStatisticsDao posStatisticsDao;

    @Resource
    private CustomerLevelSupport customerLevelSupport;

    @Override
    public ApiResult<Pagination<TransactionDailyStatisticsDto>> queryDailyStatistics(Date beginTime, Date endTime, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");

        int totalCount = posStatisticsDao.queryDailyCount(beginTime, endTime);
        Pagination<TransactionDailyStatisticsDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            pagination.setResult(posStatisticsDao.queryDailyStatistics(beginTime, endTime, limitHelper));
        }

        return ApiResult.succ(pagination);
    }

    @Override
    public ApiResult<PosGeneralStatisticsDto> queryGeneralStatistics() {
        PosGeneralStatisticsDto general = new PosGeneralStatisticsDto();

        Long customerCount = 0L;
        // 等级分布统计
        Set<Integer> levelSet = customerLevelSupport.getLevels();
        Map<Integer, Long> levelMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(levelSet)) {
            levelSet.forEach(e -> levelMap.put(e, 0L));
        }
        List<CommonEnumDto> levelDistribution = posStatisticsDao.queryLevelDistribution();
        if (!CollectionUtils.isEmpty(levelDistribution)) {
            for (CommonEnumDto e : levelDistribution) {
                levelMap.put(e.getCode().intValue(), e.getCount().longValue());
                customerCount += e.getCount().longValue();
            }
        }
        general.setLevelDistribution(levelMap);
        general.setCustomerCount(customerCount);

        // 实名认证分布统计
        Map<Integer, Long> auditMap = new HashMap<>();
        for (CustomerAuditStatus auditStatus : CustomerAuditStatus.values()) {
            auditMap.put(auditStatus.getCode(), 0L);
        }
        List<CommonEnumDto> auditDistribution = posStatisticsDao.queryAuditDistribution();
        if (!CollectionUtils.isEmpty(auditDistribution)) {
            auditDistribution.forEach(e ->
                    auditMap.put(e.getCode().intValue(), e.getCount().longValue()));
        }
        general.setAuditStatusDistribution(auditMap);

        // 收款统计
        PosGeneralStatisticsDto posStatistics = posStatisticsDao.queryPosStatistics();
        if (posStatistics != null) {
            general.setPosCustomerCount(posStatistics.getPosCustomerCount());
            general.setPosTransactionCount(posStatistics.getPosTransactionCount());
            general.setTotalPosAmount(posStatistics.getTotalPosAmount());
            general.setGrossProfit(posStatistics.getGrossProfit());
        }

        // 佣金提现统计
        PosGeneralStatisticsDto brokerageWithdrawalStatistics = posStatisticsDao.queryBrokerageWithdrawalStatistics();
        if (brokerageWithdrawalStatistics != null) {
            general.setBrokerageCustomerCount(brokerageWithdrawalStatistics.getBrokerageCustomerCount());
            general.setBrokerageWithdrawalTimes(brokerageWithdrawalStatistics.getBrokerageWithdrawalTimes());
            general.setBrokerageServiceCharge(brokerageWithdrawalStatistics.getBrokerageServiceCharge());
        }

        // 佣金统计
        PosGeneralStatisticsDto brokerageStatistics = posStatisticsDao.queryBrokerageStatistics();
        if (brokerageStatistics != null) {
            general.setTotalBrokerage(brokerageStatistics.getTotalBrokerage());
            general.setWithdrawalBrokerage(brokerageStatistics.getWithdrawalBrokerage());
            general.setNoWithdrawalBrokerage(brokerageStatistics.getNoWithdrawalBrokerage());
        }

        return ApiResult.succ(general);
    }

    @Override
    public void dailyStatisticsSchedule() {

        TransactionDailyStatisticsDto latest = posStatisticsDao.getLatest();
        if (latest == null) {
            LOG.warn("钱刷刷每日数据统计调度程序第一次执行，初始化每日数据，且只执行一次");
            initializeDailyStatistics();
            return;
        }

        LOG.info("钱刷刷每日数据统计开始......");
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        LocalDate latestDay = latest.getLocalDateKey();
        if (!latestDay.isBefore(yesterday)) {
            // 已经统计了昨日数据，不需要重复统计
            LOG.warn("已经统计了（" + yesterday.toString() + "）的每日数据，不需要重复执行......");
            return;
        }

        LocalDate beginDate = latestDay.plusDays(1);

        HashMap<LocalDate, TransactionDailyStatisticsDto> statisticsMap = queryBeginAndEndDailyStatistics(beginDate, yesterday);
        List<TransactionDailyStatisticsDto> dailies = Lists.newArrayList(statisticsMap.values());
        LOG.info("数据统计完毕，一共" + dailies.size() + "条数据");
        if (!CollectionUtils.isEmpty(dailies)) {
            LOG.info("开始入库......");
            posStatisticsDao.saveAll(dailies);
            LOG.info("入库完毕......");
        }
        LOG.info("钱刷刷每日数据统计结束");
    }

    @Override
    public void initializeDailyStatistics() {
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate beginDate = LocalDate.of(2017, 9, 1);

        // 获取开始截止时间
        Date end = Date.from(endDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        Date begin = Date.from(beginDate.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
        // 检查是否已经初始化，重复启动时不需要执行
        if (posStatisticsDao.queryDailyCount(begin, end) > 0) {
            return;
        }
        LOG.info("初始化每日数据统计，请耐心等待......");
        HashMap<LocalDate, TransactionDailyStatisticsDto> statisticsMap = queryBeginAndEndDailyStatistics(beginDate, endDate);
        List<TransactionDailyStatisticsDto> dailies = Lists.newArrayList(statisticsMap.values());
        LOG.info("数据统计完毕，一共" + dailies.size() + "条数据");
        if (!CollectionUtils.isEmpty(dailies)) {
            LOG.info("开始入库......");
            posStatisticsDao.saveAll(dailies);
            LOG.info("入库完毕......");
        }

        LOG.info("每日数据统计初始化结束");
    }

    private HashMap<LocalDate, TransactionDailyStatisticsDto> queryBeginAndEndDailyStatistics(LocalDate beginDate, LocalDate endDate) {
        // 获取开始截止时间
        Date end = Date.from(endDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        Date begin = Date.from(beginDate.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
        String beginStr = SimpleDateUtils.formatDate(begin, SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString());
        String endStr = SimpleDateUtils.formatDate(end, SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString());
        LOG.info("开始统计（" + beginStr + "--" + endStr + "）期间的每日收款数据......");
        HashMap<LocalDate, TransactionDailyStatisticsDto> statisticsMap = initializeDailyStatisticsMap(beginDate, endDate);
        List<TransactionDailyStatisticsDto> dailyPosStatistics = posStatisticsDao.queryPosStatisticsByDaily(begin, end);
        if (!CollectionUtils.isEmpty(dailyPosStatistics)) {
            dailyPosStatistics.forEach(e -> {
                LocalDate dateKey = e.getLocalDateKey();
                TransactionDailyStatisticsDto daily = statisticsMap.get(dateKey);
                if (daily == null) {
                    daily = TransactionDailyStatisticsDto.initialize(dateKey);
                    statisticsMap.putIfAbsent(dateKey, daily);
                }
                daily.setPosCustomerCount(e.getPosCustomerCount());
                daily.setPosTransactionCount(e.getPosTransactionCount());
                daily.setPosAmount(e.getPosAmount());
                daily.setGrossProfit(e.getGrossProfit());
            });
        }
        LOG.info("开始统计（" + beginStr + "--" + endStr + "）期间的每日佣金数据......");
        List<TransactionDailyStatisticsDto> dailyBrokerageStatistics = posStatisticsDao.queryBrokerageWithdrawalStatisticsByDaily(begin, end);
        if (!CollectionUtils.isEmpty(dailyBrokerageStatistics)) {
            dailyBrokerageStatistics.forEach(e -> {
                LocalDate dateKey = e.getLocalDateKey();
                TransactionDailyStatisticsDto daily = statisticsMap.get(dateKey);
                if (daily == null) {
                    daily = TransactionDailyStatisticsDto.initialize(dateKey);
                    statisticsMap.putIfAbsent(dateKey, daily);
                }
                daily.setBrokerageCustomerCount(e.getBrokerageCustomerCount());
                daily.setBrokerageWithdrawalTimes(e.getBrokerageWithdrawalTimes());
                daily.setBrokerageServiceCharge(e.getBrokerageServiceCharge());
            });
        }

        return statisticsMap;
    }

    // 初始化每日数据
    private HashMap<LocalDate, TransactionDailyStatisticsDto> initializeDailyStatisticsMap(
            LocalDate beginDate, LocalDate endDate) {
        HashMap<LocalDate, TransactionDailyStatisticsDto> map = new HashMap<>();
        int i = 0;
        while (true) {
            LocalDate dateKey = getDateKey(endDate, i++);
            if (dateKey.isBefore(beginDate)) {
                break;
            }
            map.put(dateKey, TransactionDailyStatisticsDto.initialize(dateKey));
        }

        return map;
    }

    // 获取Map的dateKey
    private LocalDate getDateKey(LocalDate endDate, int i) {
        return endDate.minusDays(i);
    }
}
