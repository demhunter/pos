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
import com.pos.pos.constants.GetAgentEnum;
import com.pos.pos.constants.PosConstants;
import com.pos.pos.constants.PosTwitterStatus;
import com.pos.pos.dao.*;
import com.pos.pos.domain.Twitter;
import com.pos.pos.dto.auth.AuthorityDto;
import com.pos.pos.dto.develop.DevelopGeneralInfoDto;
import com.pos.pos.dto.develop.PosUserChildChannelDto;
import com.pos.pos.dto.spread.SpreadCustomerDto;
import com.pos.pos.exception.PosUserErrorCode;
import com.pos.pos.service.PosUserChannelInfoService;
import com.pos.pos.dto.spread.SpreadGeneralInfoDto;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
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
    public ApiResult<SpreadGeneralInfoDto> getSpreadGeneralInfo(UserIdentifier user) {
        FieldChecker.checkEmpty(user, "user");

        // 校验推客权限
        AuthorityDto auth = authorityDao.findAuthByUserId(user.getUserId());
        if (auth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())
                || PosTwitterStatus.CLOSED.equals(auth.parseTwitterStatus())
                || !AuthStatusEnum.ENABLE.equals(auth.parseSpreadAuth())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_SPREAD);
        }
        SpreadGeneralInfoDto general = new SpreadGeneralInfoDto(user.getUserId());
        general.setRate(posConstants.getPosPoundageRate().subtract(auth.getGetRate()));
        general.setSpreadCount(posTwitterDao.queryChannelJuniorCount(user.getUserId()));

        return ApiResult.succ(general);
    }

    @Override
    public ApiResult<Pagination<SpreadCustomerDto>> querySpreadCustomers(Long channelUserId, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(channelUserId, "channelUserId");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");

        // 校验推客权限
        AuthorityDto auth = authorityDao.findAuthByUserId(channelUserId);
        if (auth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())
                || PosTwitterStatus.CLOSED.equals(auth.parseTwitterStatus())
                || !AuthStatusEnum.ENABLE.equals(auth.parseSpreadAuth())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_SPREAD);
        }

        int totalCount = posTwitterDao.queryChannelJuniorCount(channelUserId);
        Pagination<SpreadCustomerDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<SpreadCustomerDto> result = posTwitterDao.queryChannelJuniors(channelUserId, limitHelper);
            if (!CollectionUtils.isEmpty(result)) {
                List<Long> userIds = result.stream().map(SpreadCustomerDto::getJuniorUserId)
                        .collect(Collectors.toList());
                Map<Long, BigDecimal> brokerageMap = twitterBrokerageDao.queryAgentBrokerageMap(userIds);
                result.forEach(e -> {
                    if (!StringUtils.isEmpty(e.getJuniorName())) {
                        e.setJuniorName(securityService.decryptData(e.getJuniorName()));
                    }
                    if (!CollectionUtils.isEmpty(brokerageMap)) {
                        e.setBrokerage(brokerageMap.get(e.getJuniorUserId()) == null ? BigDecimal.ZERO : brokerageMap.get(e.getJuniorUserId()));
                    } else {
                        e.setBrokerage(BigDecimal.ZERO);
                    }
                });
                pagination.setResult(result);
            }
        }

        return ApiResult.succ(pagination);
    }

    @Override
    public ApiResult<DevelopGeneralInfoDto> getDevelopGeneralInfo(Long channelUserId) {
        FieldChecker.checkEmpty(channelUserId, "channelUserId");

        // 校验推客权限
        AuthorityDto auth = authorityDao.findAuthByUserId(channelUserId);
        if (auth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())
                || PosTwitterStatus.CLOSED.equals(auth.parseTwitterStatus())
                || !AuthStatusEnum.ENABLE.equals(auth.parseDevelopAuth())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_DEVELOP);
        }

        Twitter channel = posUserChannelDao.get(channelUserId);
        DevelopGeneralInfoDto general = new DevelopGeneralInfoDto();
        general.setRate(posConstants.getPosParentTwitterBrokerageRate());
        general.setDevelopCount(posUserChannelDao.getUserDevelopCount(channelUserId));
        general.setExistedParent(channel.getRelationAvailable());
        if (general.getExistedParent()) {
            general.setParentUserId(channel.getParentUserId());
            CustomerDto parent = customerService.findById(channel.getParentUserId(), true, false);
            if (parent != null) {
                String parentName = parent.getName();
                if (StringUtils.isEmpty(parentName)) {
                    parentName = parent.getUserPhone();
                }
                general.setParentName(parentName);
                general.setParentPhone(parent.getUserPhone());
            }
        }

        return ApiResult.succ(general);
    }

    @Override
    public ApiResult<Pagination<PosUserChildChannelDto>> queryDevelopTwitters(Long channelUserId, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(channelUserId, "channelUserId");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");

        // 校验推客权限
        AuthorityDto auth = authorityDao.findAuthByUserId(channelUserId);
        if (auth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())
                || PosTwitterStatus.CLOSED.equals(auth.parseTwitterStatus())
                || !AuthStatusEnum.ENABLE.equals(auth.parseDevelopAuth())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_DEVELOP);
        }

        int totalCount = posUserChannelDao.getUserDevelopCount(channelUserId);
        Pagination<PosUserChildChannelDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<PosUserChildChannelDto> result = posUserChannelDao.queryDevelopChildChannels(channelUserId, limitHelper);
            if (!CollectionUtils.isEmpty(result)) {
                List<Long> userIds = result.stream().map(PosUserChildChannelDto::getChannelUserId).collect(Collectors.toList());
                Map<Long, BigDecimal> brokerageMap = twitterBrokerageDao.queryParentAgentBrokerageMap(userIds);
                result.forEach(e -> {
                    if (!StringUtils.isEmpty(e.getChildChannelName())) {
                        e.setChildChannelName(securityService.decryptData(e.getChildChannelName()));
                    }
                    if (!CollectionUtils.isEmpty(brokerageMap)) {
                        e.setBrokerage(brokerageMap.get(e.getChannelUserId()) == null ? BigDecimal.ZERO : brokerageMap.get(e.getChannelUserId()));
                    } else {
                        e.setBrokerage(BigDecimal.ZERO);
                    }
                });
                pagination.setResult(result);
            }
        }
        return ApiResult.succ(pagination);
    }

    @Override
    public ApiResult<NullObject> updateTwitterRemark(Long developId, String remark, Long operatorUserId) {
        FieldChecker.checkEmpty(developId, "developId");
        FieldChecker.checkMaxLength(remark, 30, "remark");
        FieldChecker.checkEmpty(operatorUserId, "operatorUserId");

        // 校验推客权限
        AuthorityDto auth = authorityDao.findAuthByUserId(operatorUserId);
        if (auth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())
                || PosTwitterStatus.CLOSED.equals(auth.parseTwitterStatus())
                || !AuthStatusEnum.ENABLE.equals(auth.parseDevelopAuth())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_DEVELOP);
        }

        Twitter channel = posUserChannelDao.getById(developId);
        if (channel == null
                || !channel.getRelationAvailable()
                || !channel.getParentUserId().equals(operatorUserId)) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        channel.setRemark(remark);
        channel.setUpdateUserId(operatorUserId);
        posUserChannelDao.update(channel);

        return ApiResult.succ();
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
