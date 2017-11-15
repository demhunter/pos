/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.impl;

import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.service.SecurityService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.constants.AuthStatusEnum;
import com.pos.pos.constants.PosConstants;
import com.pos.pos.constants.PosTwitterStatus;
import com.pos.pos.dao.*;
import com.pos.pos.domain.Twitter;
import com.pos.pos.dto.auth.AuthorityDto;
import com.pos.pos.dto.develop.PosUserChildChannelDto;
import com.pos.pos.dto.spread.SpreadCustomerDto;
import com.pos.pos.dto.spread.SpreadGeneralInfoDto;
import com.pos.pos.exception.PosUserErrorCode;
import com.pos.pos.service.PosUserChannelInfoService;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 快捷收款渠道商ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosUserChannelInfoServiceImpl implements PosUserChannelInfoService {

    @Resource
    private PosUserChannelDao posUserChannelDao;

    @Resource
    private PosUserTransactionRecordDao posUserTransactionRecordDao;

    @Resource
    private AuthorityDao authorityDao;

    @Resource
    private TwitterBrokerageDao twitterBrokerageDao;

    @Resource
    private PosConstants posConstants;

    @Resource
    private PosTwitterDao posTwitterDao;

    @Resource
    private CustomerService customerService;

    @Value("${pos.helibao.tixian.leader}")
    private String withdrawDepositRate;

    @Resource
    private SecurityService securityService;

    @Override
    public Twitter get(Long channelUserId) {
        FieldChecker.checkEmpty(channelUserId, "channelUserId");

        return posUserChannelDao.get(channelUserId);
    }

    @Override
    public Integer getUserDevelopCount(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        return posUserChannelDao.getUserDevelopCount(userId);
    }


    private BigDecimal getWithdrawDepositRate() {
        return new BigDecimal(withdrawDepositRate);
    }
}
