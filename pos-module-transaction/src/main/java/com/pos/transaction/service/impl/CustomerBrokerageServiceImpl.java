/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.impl;

import com.google.common.collect.Lists;
import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.authority.dto.statistics.CustomerStatisticsDto;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.transaction.constants.BrokerageStatusType;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.constants.TransactionType;
import com.pos.transaction.dao.CustomerBrokerageDao;
import com.pos.transaction.dto.brokerage.BrokerageDailyStatisticsDto;
import com.pos.transaction.dto.brokerage.BrokerageGeneralInfoDto;
import com.pos.transaction.exception.TransactionErrorCode;
import com.pos.transaction.service.CustomerBrokerageService;
import com.pos.transaction.service.PosService;
import com.pos.user.exception.UserErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 客户佣金ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerBrokerageServiceImpl implements CustomerBrokerageService {

    @Resource
    private PosConstants posConstants;

    @Resource
    private CustomerStatisticsService customerStatisticsService;

    @Resource
    private CustomerAuthorityService customerAuthorityService;

    @Resource
    private PosService posService;

    @Resource
    private CustomerBrokerageDao customerBrokerageDao;

    @Override
    public BrokerageGeneralInfoDto getBrokerageGeneral(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        BrokerageGeneralInfoDto general = new BrokerageGeneralInfoDto();

        CustomerStatisticsDto statistics = customerStatisticsService.getStatistics(userId);
        if (statistics != null) {
            general.setAppliedBrokerage(statistics.getWithdrawalBrokerage());
            BigDecimal canApplyBrokerage = statistics.getTotalBrokerage()
                    .subtract(statistics.getWithdrawalBrokerage());
            if (canApplyBrokerage.compareTo(BigDecimal.ZERO) < 0) {
                canApplyBrokerage = BigDecimal.ZERO;
            }
            general.setCanApplyBrokerage(canApplyBrokerage);
        }
        Date now = new Date();

        BigDecimal todayBrokerage = customerBrokerageDao.getDateSectionBrokerage(
                userId,
                SimpleDateUtils.getDateOfMidNight(now),
                SimpleDateUtils.getDateOfTodayEnd(now));
        general.setTodayBrokerage(todayBrokerage == null ? BigDecimal.ZERO : todayBrokerage);

        return general;
    }

    @Override
    public List<BrokerageDailyStatisticsDto> queryDailyBrokerage(Long userId, LimitHelper limitHelper, boolean queriedMonth) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");

        List<BrokerageDailyStatisticsDto> dailyBrokerages = customerBrokerageDao.queryDailyBrokerage(userId, limitHelper);
        List<BrokerageDailyStatisticsDto> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(dailyBrokerages)) {
            if (!queriedMonth) {
                result.addAll(dailyBrokerages);
            } else {
                // 有每日数据，插入月份分栏统计
                int yearCursor = 0; // 年标记
                int monthCursor = 0; // 月标记
                for (BrokerageDailyStatisticsDto dailyBrokerage : dailyBrokerages) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dailyBrokerage.getDateKey());
                    // 获取当前记录日期的年月
                    int currentYear = calendar.get(Calendar.YEAR);
                    int currentMonth = calendar.get(Calendar.MONTH);
                    // 当记录日期的年月不是标记的年月时，统计当前日期所在月的佣金
                    if (yearCursor != currentYear || monthCursor != currentMonth) {
                        // 更新标记年月
                        yearCursor = currentYear;
                        monthCursor = currentMonth;
                        // 获取当前月份的开始
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        Date begin = SimpleDateUtils.getDateOfMidNight(calendar.getTime());
                        // 获取当前月份的结束
                        calendar.add(Calendar.MONTH, 1);
                        calendar.set(Calendar.DAY_OF_MONTH, 0);
                        Date end = SimpleDateUtils.getDateOfTodayEnd(calendar.getTime());
                        // 查询起止时间的佣金统计
                        BigDecimal monthBrokerage = customerBrokerageDao.getDateSectionBrokerage(userId, begin, end);
                        // 插入月份分栏
                        BrokerageDailyStatisticsDto monthStatistics = new BrokerageDailyStatisticsDto();
                        monthStatistics.setDateKey(end);
                        monthStatistics.setBrokerage(monthBrokerage);
                        monthStatistics.setMonthStatistics(true);
                        result.add(monthStatistics);
                    }
                    // 插入每日佣金
                    result.add(dailyBrokerage);
                }
            }
        }

        return result;
    }

    @Override
    public ApiResult<BigDecimal> withdrawBrokerage(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");
        Date deadline = new Date();

        // 状态校验
        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);
        if (permission == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        CustomerAuditStatus auditStatus = permission.parseAuditStatus();
        if (CustomerAuditStatus.NOT_SUBMIT.equals(auditStatus)
                || CustomerAuditStatus.REJECTED.equals(auditStatus)) {
            return ApiResult.fail(TransactionErrorCode.BROKERAGE_ERROR_AUTHORITY_AUDIT_STATUS);
        }
        // 佣金金额限制校验
        BigDecimal brokerage = customerBrokerageDao.queryCanWithdrawBrokerage(userId, deadline);
        brokerage = brokerage == null ? BigDecimal.ZERO : brokerage;
        if (brokerage.compareTo(posConstants.getBrokerageWithdrawDownLimit()) < 0) {
            return ApiResult.failFormatMsg(
                    TransactionErrorCode.BROKERAGE_ERROR_BROKERAGE_NOT_ENOUGH,
                    posConstants.getBrokerageWithdrawDownLimit());
        }
        // 标记佣金提现状态
        customerBrokerageDao.markBrokerageStatus(
                userId,
                BrokerageStatusType.NOT_WITHDRAW.getCode(),
                BrokerageStatusType.WITHDRAWING.getCode(),
                deadline);
        // 累计已提现佣金
        customerStatisticsService.incrementWithdrawalBrokerage(userId, brokerage);
        // 发起佣金提现交易
        return posService.withdrawBrokerage(permission, brokerage);
    }
}
