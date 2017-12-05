/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.impl;

import com.pos.common.util.mvc.support.*;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.service.SecurityService;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.validation.FieldChecker;
import com.pos.transaction.condition.query.PosUserCondition;
import com.pos.transaction.dao.PosUserChannelDao;
import com.pos.transaction.domain.UserPosChannelInfo;
import com.pos.transaction.dto.transaction.TransactionSimpleStatisticsDto;
import com.pos.transaction.dto.user.PosUserIntegrateDto;
import com.pos.transaction.service.PosUserService;
import com.pos.transaction.condition.orderby.PosUserOrderField;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.constants.PosTwitterStatus;
import com.pos.transaction.dao.PosAuthDao;
import com.pos.transaction.dao.PosUserJuniorDao;
import com.pos.transaction.dao.PosUserTransactionRecordDao;
import com.pos.transaction.domain.UserPosJuniorInfo;
import com.pos.transaction.dto.auth.BaseAuthDto;
import com.pos.transaction.dto.auth.PosUserAuthDetailDto;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 快捷收款用户Service
 *
 * @author wangbing
 * @version 1.0, 2017/8/25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosUserServiceImpl implements PosUserService {

    @Resource
    private PosAuthDao posAuthDao;

    @Resource
    private CustomerService customerService;

    @Resource
    private PosUserChannelDao posUserChannelDao;

    @Resource
    private PosUserJuniorDao posUserJuniorDao;

    @Resource
    private PosUserTransactionRecordDao posUserTransactionRecordDao;

    @Resource
    private SecurityService securityService;

    @Resource
    private PosConstants posConstants;

    @Resource
    private SmsService smsService;

    @Override
    public Pagination<PosUserIntegrateDto> queryPosUsers(PosUserCondition condition, OrderHelper orderHelper, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(PosUserOrderField.getInstance());
        }

        int totalCount = posAuthDao.getPosUserCount(condition);
        Pagination<PosUserIntegrateDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<PosUserIntegrateDto> result = posAuthDao.queryPosUsers(condition, orderHelper, limitHelper);
            if (!CollectionUtils.isEmpty(result)) {
                List<Long> userIds = result.stream().map(PosUserIntegrateDto::getUserId).collect(Collectors.toList());
                Map<Long, CustomerDto> customerMap = customerService.getCustomerDtoMapById(userIds);
                List<TransactionSimpleStatisticsDto> statisticsList = posUserTransactionRecordDao.querySimpleStatistics(userIds);
                Map<Long, TransactionSimpleStatisticsDto> statisticsMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(statisticsList)) {
                    statisticsList.forEach(e -> statisticsMap.put(e.getUserId(), e));
                }
                result.forEach(e -> {
                    e.setPoundage(posConstants.getPosExtraPoundage());
                    e.setPosUserInfo(customerMap.get(e.getUserId()));
                    TransactionSimpleStatisticsDto statistics = statisticsMap.get(e.getUserId());
                    if (statistics == null) {
                        e.setUserPosCount(0);
                        e.setUserPosAmount(BigDecimal.ZERO);
                    } else {
                        e.setUserPosCount(statistics.getTransactionCount());
                        e.setUserPosAmount(statistics.getTransactionAmount());
                    }
                    decryptPosCardInfo(e);
                });
            }
            pagination.setResult(result);
        }

        return pagination;
    }

    /**
     * 解密银行卡信息<br/>
     * 此解密返回一个新的对象，需要调用者接收并保存，原对象不变
     *
     * @param source 需要被解密的银行卡
     * @return 解密后的银行卡信息
     */
    private void decryptPosCardInfo(PosUserIntegrateDto source) {
        if (StringUtils.isNotEmpty(source.getBankCardName())) {
            source.setBankCardName(securityService.decryptData(source.getBankCardName()));
        }
        if (StringUtils.isNotEmpty(source.getBankCardIdCardNO())) {
            source.setBankCardIdCardNO(securityService.decryptData(source.getBankCardIdCardNO()));
        }
        if (StringUtils.isNotEmpty(source.getBankCardNO())) {
            source.setBankCardNO(securityService.decryptData(source.getBankCardNO()));
        }
        if (StringUtils.isNotEmpty(source.getMobilePhone())) {
            source.setMobilePhone(securityService.decryptData(source.getMobilePhone()));
        }
    }

    @Override
    public ApiResult<NullObject> updatePosUserAuth(Long posId, BaseAuthDto baseAuth, UserIdentifier user) {
        FieldChecker.checkEmpty(baseAuth, "baseAuth");
        FieldChecker.checkEmpty(user, "user");
        FieldChecker.checkMinValue(baseAuth.getGetRate(), posConstants.getPosPoundageRateDownLimit(), "getRate");

        PosUserAuthDetailDto oldAuth = posAuthDao.findAuthDetailById(posId);
        if (oldAuth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(oldAuth.parseTwitterStatus())
                && PosTwitterStatus.CLOSED.equals(baseAuth.parseTwitterStatus())) {
            // 特殊处理，未启用状态 -> 已关闭状态 视为没有操作更新推客状态
            baseAuth.setTwitterStatus(oldAuth.parseTwitterStatus().getCode());
        }
        posAuthDao.updateAuth(posId, baseAuth, user.getUserId());
        // 开通推客功能权限需要特殊处理，第一次开通推客功能需要新增推客记录
        if (PosTwitterStatus.ENABLE.equals(baseAuth.parseTwitterStatus())) {
            UserPosChannelInfo channel = posUserChannelDao.get(oldAuth.getUserId());
            if (channel == null) {
                // 查询是否有关联上级推客
                UserPosJuniorInfo junior = posUserJuniorDao.getJuniorByJuniorUserId(oldAuth.getUserId());
                if (junior != null && junior.getRelationAvailable()) {
                    // 解除关联关系
                    junior.setRelationAvailable(Boolean.FALSE);
                    junior.setUpdateUserId(user.getUserId());
                    posUserJuniorDao.updateJunior(junior);
                }
                CustomerDto customer = customerService.findById(oldAuth.getUserId(), true, false);
                channel = new UserPosChannelInfo();
                channel.setParentUserId(0L); // M端开通没有上级业务员
                channel.setChannelUserId(oldAuth.getUserId());
                channel.setChannelPhone(customer.getUserPhone());
                posUserChannelDao.save(channel);
            }
        } else if (PosTwitterStatus.CLOSED.equals(baseAuth.parseTwitterStatus())) {
            UserPosChannelInfo channel = posUserChannelDao.get(oldAuth.getUserId());
            if (channel != null && channel.getRelationAvailable()) {
                channel.setParentUserId(0L);
                channel.setRelationAvailable(Boolean.FALSE); // 解除与上级推客的关联关系
                channel.setUpdateUserId(user.getUserId());
                posUserChannelDao.update(channel);

                // 解除下级客户关系
                List<UserPosJuniorInfo> juniors = posUserJuniorDao.queryJuniorsByChannelUserId(oldAuth.getUserId());
                if (!CollectionUtils.isEmpty(juniors)) {
                    juniors.forEach(junior -> {
                        junior.setRelationAvailable(Boolean.FALSE);
                        junior.setUpdateUserId(user.getUserId());
                        posUserJuniorDao.updateJunior(junior);
                    });
                }
            }
        }
        // 判断推客权限和收款费率是否变更，发送变更短信
        CustomerDto customer = customerService.findById(oldAuth.getUserId(), true, true);
        if (customer != null) {
            if (!oldAuth.parseTwitterStatus().equals(baseAuth.parseTwitterStatus())) {
                if (PosTwitterStatus.CLOSED.equals(baseAuth.parseTwitterStatus())) {
                    // 推客权限关闭
                    String closedMsg = posConstants.getPosTwitterStatusDisableTemplate();
                    smsService.sendMessage(customer.getUserPhone(), closedMsg);
                }
                if (PosTwitterStatus.ENABLE.equals(baseAuth.parseTwitterStatus())) {
                    // 推客权限开通
                    String enableMsg = posConstants.getPosTwitterStatusEnableTemplate();
                    smsService.sendMessage(customer.getUserPhone(), enableMsg);
                }
            }
            if (!oldAuth.getGetRate().equals(baseAuth.getGetRate())) {
                // 收款费率变更
                String getRateMsg = String.format(
                        posConstants.getPosUserGetRateChangedTemplate(),
                        baseAuth.getGetRate().multiply(new BigDecimal("100"), new MathContext(2)),
                        posConstants.getPosExtraPoundage());
                smsService.sendMessage(customer.getUserPhone(), getRateMsg);
            }
        }


        return ApiResult.succ();
    }

    @Override
    public ApiResult<BaseAuthDto> getBaseAuthById(Long posId) {
        FieldChecker.checkEmpty(posId, "posId");

        BaseAuthDto baseAuth = posAuthDao.findBaseAuthById(posId);
        if (baseAuth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        baseAuth.setPoundage(posConstants.getPosExtraPoundage());

        return ApiResult.succ(baseAuth);
    }
}
