/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service_v.impl;

import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.service.SecurityService;
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
import com.pos.pos.dao.AuthorityDao;
import com.pos.pos.dao.TwitterBrokerageHandledDao;
import com.pos.pos.dao.TwitterBrokerageDao;
import com.pos.pos.dao.PosTwitterDao;
import com.pos.pos.domain.Twitter;
import com.pos.pos.domain.TwitterCustomer;
import com.pos.pos.domain.TwitterRelation;
import com.pos.pos.dto.BrokerageHandledRecordDto;
import com.pos.pos.dto.auth.AuthorityDto;
import com.pos.pos.dto.develop.DevelopGeneralInfoDto;
import com.pos.pos.dto.develop.PosUserChildChannelDto;
import com.pos.pos.dto.spread.SpreadCustomerDto;
import com.pos.pos.dto.spread.SpreadGeneralInfoDto;
import com.pos.pos.dto.twitter.TwitterDailyStatisticsDto;
import com.pos.pos.dto.twitter.TwitterGeneralInfoDto;
import com.pos.pos.exception.PosUserErrorCode;
import com.pos.pos.service_v.TwitterService;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
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
 * 推客相关Service实现类
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwitterServiceImpl implements TwitterService {

    @Resource
    private CustomerService customerService;

    @Resource
    private SecurityService securityService;

    @Resource
    private PosTwitterDao posTwitterDao;

    @Resource
    private AuthorityDao authorityDao;

    @Resource
    private TwitterBrokerageDao twitterBrokerageDao;

    @Resource
    private TwitterBrokerageHandledDao twitterBrokerageHandledDao;

    @Resource
    private PosConstants posConstants;

    @Override
    public void initializeTwitter(Long userId, Long leaderUserId) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(leaderUserId, "leaderUserId");

        // 保存推客信息
        Twitter child = new Twitter();
        child.setUserId(userId);
        child.setTotalMoney(BigDecimal.ZERO);
        child.setApplyMoney(BigDecimal.ZERO);
        posTwitterDao.saveTwitter(child);

        // 保存推客上下级关联关系
        Twitter parent = posTwitterDao.getTwitterByUserId(leaderUserId);
        if (parent != null) {
            TwitterRelation relation = new TwitterRelation();
            relation.setParentTwitterId(parent.getId());
            relation.setTwitterId(child.getId());
            relation.setAvailable(Boolean.TRUE);
            posTwitterDao.saveTwitterRelation(relation);
        }

    }

    @Override
    public void initializeTwitterCustomer(Long userId, Long leaderUserId) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(leaderUserId, "leaderUserId");

        Twitter parent = posTwitterDao.getTwitterByUserId(leaderUserId);
        if (parent != null) {
            // 推客发展下级客户链接进入，且上级推客的发展下级客户权限处于启用状态
            // 则绑定推客客户上下级关系
            TwitterCustomer junior = new TwitterCustomer();
            junior.setTwitterId(parent.getId());
            junior.setUserId(userId);
            junior.setAvailable(Boolean.TRUE);
            posTwitterDao.saveTwitterCustomer(junior);
        }

    }

    @Override
    public ApiResult<TwitterGeneralInfoDto> queryTwitterGeneralInfo(Long twitterUserId) {
        FieldChecker.checkEmpty(twitterUserId, "twitterUserId");

        // 校验推客权限
        AuthorityDto auth = authorityDao.findAuthByUserId(twitterUserId);
        if (auth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_BROKERAGE);
        }
        // 获取推客已提现和当前提现金额
        Twitter twitter = posTwitterDao.getTwitterByUserId(twitterUserId);
        TwitterGeneralInfoDto result = new TwitterGeneralInfoDto();
        result.setTotalApplyMoney(twitter.getTotalMoney());
        result.setCurrentApplyMoney(twitter.getApplyMoney());
        // 维护数据一致性，设置查询和状态更新的截止时间
        Date deadline = new Date();
        // 获取推客可提现余额
        BigDecimal canApplyMoney = twitterBrokerageDao.queryTwitterCanApplyMoney(twitterUserId, deadline);
        result.setCanApplyMoney(canApplyMoney == null ? BigDecimal.ZERO : canApplyMoney);
        // 获取推客的今日收益
        BigDecimal todayBrokerage = twitterBrokerageDao.queryTwitterDateSectionBrokerage(twitterUserId,
                SimpleDateUtils.getDateOfMidNight(deadline), SimpleDateUtils.getDateOfTodayEnd(deadline));
        result.setTodayBrokerage(todayBrokerage == null ? BigDecimal.ZERO : todayBrokerage);

        return ApiResult.succ(result);
    }

    @Override
    public List<TwitterDailyStatisticsDto> queryTwitterDailyStatistics(Long twitterUserId, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(twitterUserId, "twitterUserId");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");


        return twitterBrokerageDao.queryTwitterDailyStatistics(twitterUserId, limitHelper);
    }

    @Override
    public ApiResult<BigDecimal> applyWithdrawBrokerage(UserIdentifier user) {
        FieldChecker.checkEmpty(user, "user");

        Twitter twitter = posTwitterDao.getTwitterByUserId(user.getUserId());
        if (twitter == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        BigDecimal currentApplyMoney = twitter.getApplyMoney() == null ? BigDecimal.ZERO : twitter.getApplyMoney();
        if (currentApplyMoney.compareTo(BigDecimal.ZERO) > 0) {
            return ApiResult.fail(PosUserErrorCode.CURRENT_APPLY_MONEY_NOT_EMPTY);
        }
        // 维护数据一致性，设置查询和状态更新的截止时间
        Date deadline = new Date();
        currentApplyMoney = twitterBrokerageDao.queryTwitterCanApplyMoney(user.getUserId(), deadline);
        currentApplyMoney = currentApplyMoney == null ? BigDecimal.ZERO : currentApplyMoney;
        if (currentApplyMoney.compareTo(BigDecimal.ZERO) <= 0) {
            return ApiResult.fail(PosUserErrorCode.CURRENT_APPLY_MONEY_IS_ZERO);
        }
        // 标记佣金已申请提现
        twitterBrokerageDao.markTwitterStatus(user.getUserId(),
                GetAgentEnum.NOT_GET.getCode(), GetAgentEnum.APPLY.getCode(), deadline);
        twitterBrokerageDao.markParentStatus(user.getUserId(),
                GetAgentEnum.NOT_GET.getCode(), GetAgentEnum.APPLY.getCode(), deadline);
        // 更新提现申请金额
        twitter.setApplyMoney(currentApplyMoney);
        twitter.setUpdateUserId(user.getUserId());
        posTwitterDao.update(twitter);

        return ApiResult.succ(currentApplyMoney);
    }

    @Override
    public List<BrokerageHandledRecordDto> queryBrokerageHandledRecord(UserIdentifier user, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(user, "user");

        return twitterBrokerageHandledDao.queryHandledRecords(user, limitHelper);
    }

    @Override
    public ApiResult<DevelopGeneralInfoDto> getDevelopGeneralInfo(Long twitterUserId) {
        FieldChecker.checkEmpty(twitterUserId, "channelUserId");

        // 校验推客权限
        AuthorityDto auth = authorityDao.findAuthByUserId(twitterUserId);
        if (auth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())
                || PosTwitterStatus.CLOSED.equals(auth.parseTwitterStatus())
                || !AuthStatusEnum.ENABLE.equals(auth.parseDevelopAuth())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_DEVELOP);
        }

        Twitter channel = posTwitterDao.getTwitterByUserId(twitterUserId);
        DevelopGeneralInfoDto general = new DevelopGeneralInfoDto();
        general.setRate(posConstants.getPosParentTwitterBrokerageRate());
        general.setDevelopCount(posTwitterDao.getDevelopCount(twitterUserId));
        general.setExistedParent(channel.getRelationAvailable());
        if (general.getExistedParent()) {
            general.setParentUserId(channel.getParentUserId());
            CustomerDto parent = customerService.findById(channel.getParentUserId(), true);
            if (parent != null) {
                String parentName = parent.getRealName();
                if (StringUtils.isEmpty(parentName)) {
                    parentName = parent.getPhone();
                }
                general.setParentName(parentName);
                general.setParentPhone(parent.getPhone());
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

        int totalCount = posTwitterDao.getDevelopCount(channelUserId);
        Pagination<PosUserChildChannelDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<PosUserChildChannelDto> result = posTwitterDao.queryDevelopChildTwitter(channelUserId, limitHelper);
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
    public ApiResult<NullObject> updateChildTwitterRemark(Long developId, String remark, Long operatorUserId) {
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

        Twitter channel = posTwitterDao.getTwitterById(developId);
        if (channel == null
                || !channel.getRelationAvailable()
                || !channel.getParentUserId().equals(operatorUserId)) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        channel.setRemark(remark);
        channel.setUpdateUserId(operatorUserId);
        posTwitterDao.update(channel);

        return ApiResult.succ();
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
        general.setSpreadCount(posTwitterDao.queryCustomerCountByTwitterUserId(user.getUserId()));

        return ApiResult.succ(general);
    }

    @Override
    public ApiResult<Pagination<SpreadCustomerDto>> querySpreadCustomers(Long twitterUserId, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(twitterUserId, "twitterUserId");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");

        // 校验推客权限
        AuthorityDto auth = authorityDao.findAuthByUserId(twitterUserId);
        if (auth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(auth.parseTwitterStatus())
                || PosTwitterStatus.CLOSED.equals(auth.parseTwitterStatus())
                || !AuthStatusEnum.ENABLE.equals(auth.parseSpreadAuth())) {
            return ApiResult.fail(PosUserErrorCode.TWITTER_PERMISSION_ERROR_FOR_SPREAD);
        }

        int totalCount = posTwitterDao.queryCustomerCountByTwitterUserId(twitterUserId);
        Pagination<SpreadCustomerDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<SpreadCustomerDto> result = posTwitterDao.queryCustomersByTwitterUserId(twitterUserId, limitHelper);
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
}
