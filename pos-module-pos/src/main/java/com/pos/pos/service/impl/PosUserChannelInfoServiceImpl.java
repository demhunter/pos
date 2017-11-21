/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.impl;

import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.service.SecurityService;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.date.SimpleDateUtils;
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
import com.pos.pos.domain.UserPosChannelInfo;
import com.pos.pos.dto.auth.PosUserAuthDto;
import com.pos.pos.dto.develop.DevelopGeneralInfoDto;
import com.pos.pos.dto.develop.PosUserChildChannelDto;
import com.pos.pos.dto.spread.SpreadCustomerDto;
import com.pos.pos.dto.spread.SpreadGeneralInfoDto;
import com.pos.pos.dto.twitter.ReferrerSimpleDto;
import com.pos.pos.dto.twitter.TwitterBrokerageStatisticsDto;
import com.pos.pos.dto.twitter.TwitterDailyStatisticsDto;
import com.pos.pos.dto.twitter.TwitterGeneralInfoDto;
import com.pos.pos.exception.PosUserErrorCode;
import com.pos.pos.service.PosUserChannelInfoService;
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
import java.util.HashMap;
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
    private PosAuthDao posAuthDao;

    @Resource
    private PosTwitterBrokerageDao posTwitterBrokerageDao;

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
    public UserPosChannelInfo get(Long channelUserId) {
        FieldChecker.checkEmpty(channelUserId, "channelUserId");

        return posUserChannelDao.get(channelUserId);
    }

    @Override
    public ApiResult<TwitterGeneralInfoDto> queryTwitterGeneralInfo(Long channelUserId) {
        FieldChecker.checkEmpty(channelUserId, "channelUserId");

        // 校验推客权限
        PosUserAuthDto auth = posAuthDao.findAuth(channelUserId);
        if (auth == null ) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_BROKERAGE);
        }
        // 获取推客已提现和当前提现金额
        UserPosChannelInfo channelInfo = posUserChannelDao.get(channelUserId);
        TwitterGeneralInfoDto result = new TwitterGeneralInfoDto();
        result.setTotalApplyMoney(channelInfo.getTotalMoney());
        result.setCurrentApplyMoney(channelInfo.getApplyMoney());
        // 维护数据一致性，设置查询和状态更新的截止时间
        Date deadline = new Date();
        // 获取推客可提现余额
        BigDecimal canApplyMoney = posTwitterBrokerageDao.queryTwitterCanApplyMoney(channelUserId, deadline);
        result.setCanApplyMoney(canApplyMoney == null ? BigDecimal.ZERO : canApplyMoney);
        // 获取推客的今日收益
        BigDecimal todayBrokerage = posTwitterBrokerageDao.queryTwitterDateSectionBrokerage(channelUserId,
                SimpleDateUtils.getDateOfMidNight(deadline), SimpleDateUtils.getDateOfTodayEnd(deadline));
        result.setTodayBrokerage(todayBrokerage == null ? BigDecimal.ZERO : todayBrokerage);

        return ApiResult.succ(result);
    }

    @Override
    public List<TwitterDailyStatisticsDto> queryTwitterDailyStatistics(Long channelUserId, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(channelUserId, "channelUserId");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");


        return posTwitterBrokerageDao.queryTwitterDailyStatistics(channelUserId, limitHelper);
    }

    @Override
    public ApiResult<SpreadGeneralInfoDto> getSpreadGeneralInfo(UserIdentifier user) {
        FieldChecker.checkEmpty(user, "user");

        // 校验推客权限
        PosUserAuthDto auth = posAuthDao.findAuth(user.getUserId());
        if (auth == null ) {
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
        PosUserAuthDto auth = posAuthDao.findAuth(channelUserId);
        if (auth == null ) {
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
                Map<Long, BigDecimal> brokerageMap = new HashMap<>();
                List<TwitterBrokerageStatisticsDto> brokerages = posTwitterBrokerageDao.queryAgentBrokerageMap(userIds);
                if (!CollectionUtils.isEmpty(brokerages)) {
                    brokerages.forEach(e -> brokerageMap.put(e.getUserId(), e.getBrokerage()));
                }
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
        PosUserAuthDto auth = posAuthDao.findAuth(channelUserId);
        if (auth == null ) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())
                || PosTwitterStatus.CLOSED.equals(auth.parseTwitterStatus())
                || !AuthStatusEnum.ENABLE.equals(auth.parseDevelopAuth())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_DEVELOP);
        }

        UserPosChannelInfo channel = posUserChannelDao.get(channelUserId);
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
    public ApiResult<BigDecimal> applyWithdrawBrokerage(UserIdentifier user) {
        FieldChecker.checkEmpty(user, "user");

        UserPosChannelInfo channel = posUserChannelDao.get(user.getUserId());
        if (channel == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        BigDecimal currentApplyMoney = channel.getApplyMoney() == null ? BigDecimal.ZERO : channel.getApplyMoney();
        if (currentApplyMoney.compareTo(BigDecimal.ZERO) > 0) {
            return ApiResult.fail(PosUserErrorCode.CURRENT_APPLY_MONEY_NOT_EMPTY);
        }
        // 维护数据一致性，设置查询和状态更新的截止时间
        Date deadline = new Date();
        currentApplyMoney = posTwitterBrokerageDao.queryTwitterCanApplyMoney(user.getUserId(), deadline);
        currentApplyMoney = currentApplyMoney == null ? BigDecimal.ZERO : currentApplyMoney;
        if (currentApplyMoney.compareTo(new BigDecimal("10.00")) < 0) {
            return ApiResult.fail(PosUserErrorCode.CURRENT_APPLY_MONEY_LESS_THAN_TEN);
        }
        // 标记佣金已申请提现
        posTwitterBrokerageDao.markAgentStatus(user.getUserId(),
                GetAgentEnum.NOT_GET.getCode(), GetAgentEnum.APPLY.getCode(), deadline);
        posTwitterBrokerageDao.markParentAgentStatus(user.getUserId(),
                GetAgentEnum.NOT_GET.getCode(), GetAgentEnum.APPLY.getCode(), deadline);
        // 更新提现申请金额
        channel.setApplyMoney(currentApplyMoney);
        posUserChannelDao.update(channel);

        return ApiResult.succ(currentApplyMoney);
    }

    @Override
    public ApiResult<Pagination<PosUserChildChannelDto>> queryDevelopTwitters(Long channelUserId, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(channelUserId, "channelUserId");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");

        // 校验推客权限
        PosUserAuthDto auth = posAuthDao.findAuth(channelUserId);
        if (auth == null ) {
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
                Map<Long, BigDecimal> brokerageMap = new HashMap<>();
                List<TwitterBrokerageStatisticsDto> brokerages = posTwitterBrokerageDao.queryParentAgentBrokerageMap(userIds);
                if (!CollectionUtils.isEmpty(brokerages)) {
                    brokerages.forEach(e -> brokerageMap.put(e.getUserId(), e.getBrokerage()));
                }
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
        PosUserAuthDto auth = posAuthDao.findAuth(operatorUserId);
        if (auth == null ) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())
                || PosTwitterStatus.CLOSED.equals(auth.parseTwitterStatus())
                || !AuthStatusEnum.ENABLE.equals(auth.parseDevelopAuth())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_DEVELOP);
        }

        UserPosChannelInfo channel = posUserChannelDao.getById(developId);
        if (channel == null
                || !channel.getRelationAvailable()
                || !channel.getParentUserId().equals(operatorUserId)) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        channel.setChannelRemark(remark);
        channel.setUpdateUserId(operatorUserId);
        posUserChannelDao.update(channel);

        return ApiResult.succ();
    }


    @Override
    public ApiResult<ReferrerSimpleDto> findReferrerSimpleInfo(Long referrerUserId) {
        FieldChecker.checkEmpty(referrerUserId, "referrerUserId");

        PosUserAuthDto userAuth = posAuthDao.findAuth(referrerUserId);
        CustomerDto customer = customerService.findById(referrerUserId, true, true);
        if (userAuth == null || customer == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        ReferrerSimpleDto referrer = new ReferrerSimpleDto();
        referrer.setUserId(referrerUserId);
        referrer.setReferrerName(userAuth.getIdCardName());
        referrer.setReferrerPhone(customer.getUserPhone());

        // 解密并隐藏推荐人部分姓名信息
        if (!StringUtils.isEmpty(referrer.getReferrerName())) {
            String name = securityService.decryptData(referrer.getReferrerName());
            referrer.setReferrerName(SimpleRegexUtils.hiddenName(name));
        }
        // 隐藏电话号码中间四位
        if (!StringUtils.isEmpty(referrer.getReferrerPhone())) {
            referrer.setReferrerPhone(SimpleRegexUtils.hiddenMobile(referrer.getReferrerPhone()));
        }

        return ApiResult.succ(referrer);
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
