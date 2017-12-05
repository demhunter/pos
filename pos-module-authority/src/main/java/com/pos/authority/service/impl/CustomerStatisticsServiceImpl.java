/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.impl;

import com.pos.authority.dao.CustomerStatisticsDao;
import com.pos.authority.dto.statistics.CustomerStatisticsDto;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.common.util.validation.FieldChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 客户统计ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerStatisticsServiceImpl implements CustomerStatisticsService {

    @Resource
    private CustomerStatisticsDao customerStatisticsDao;

    @Override
    public CustomerStatisticsDto getStatistics(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        return customerStatisticsDao.getByUserId(userId);
    }
}
