/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.impl;

import com.pos.authority.dto.statistics.CustomerStatisticsDto;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.dao.CustomerBrokerageDao;
import com.pos.pos.dto.brokerage.BrokerageGeneralInfoDto;
import com.pos.pos.service.CustomerBrokerageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

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
    private CustomerStatisticsService customerStatisticsService;

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
}
