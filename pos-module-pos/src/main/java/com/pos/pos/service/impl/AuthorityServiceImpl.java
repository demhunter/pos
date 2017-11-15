/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.impl;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.constants.*;
import com.pos.pos.dao.AuthorityDao;
import com.pos.pos.dao.PosTwitterDao;
import com.pos.pos.domain.Authority;
import com.pos.pos.domain.Twitter;
import com.pos.pos.domain.TwitterCustomer;
import com.pos.pos.dto.auth.AuthorityDto;
import com.pos.pos.dto.auth.BaseAuthDto;
import com.pos.pos.dto.auth.AuthorityDetailDto;
import com.pos.pos.service.AuthorityService;
import com.pos.pos.service.TwitterService;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.login.RegisterRecommendDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * POS权限控制Service实现类
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AuthorityServiceImpl implements AuthorityService {

    private final static Logger LOG = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    @Resource
    private PosConstants posConstants;

    @Resource
    private AuthorityDao authorityDao;

    @Resource
    private PosTwitterDao posTwitterDao;

    @Resource
    private CustomerService customerService;

    @Resource
    private SmsService smsService;

    @Resource
    private TwitterService twitterService;

    @Override
    public void initializeAuthority(Long userId, RegisterRecommendDto recommend) {
        FieldChecker.checkEmpty(userId, "userId");

        CustomerDto customer = customerService.findById(userId, true);
        if (customer == null) {
            LOG.error("用户信息不存在，无法初始化权限信息!");
        } else {
            // 获取推荐信息
            AuthorityDto leaderUserPosAuth = getLeaderAuthority(recommend);
            LoginTypeEnum loginType = parsePosLoginType(recommend);
            // 初始化权限和收款费率
            initializeAuthority(customer.getId(), loginType, leaderUserPosAuth);
            // 初始关系类型
            if (loginType != null && leaderUserPosAuth != null) {
                initializeRelation(userId, loginType, leaderUserPosAuth);
            }
        }
    }

    private AuthorityDto getLeaderAuthority(RegisterRecommendDto recommend) {
        AuthorityDto leaderAuthority = null;
        if (recommend != null && recommend.getRecommendType() != null && recommend.getRecommendUserId() != null) {
            leaderAuthority = authorityDao.findAuthByUserId(recommend.getRecommendUserId());
        }
        return leaderAuthority;
    }

    private LoginTypeEnum parsePosLoginType(RegisterRecommendDto recommend) {
        LoginTypeEnum loginType = null;
        if (recommend != null && recommend.getRecommendType() != null && recommend.getRecommendUserId() != null) {
            loginType = LoginTypeEnum.getEnum(recommend.getRecommendType().byteValue());
        }
        return loginType;
    }

    // 首次登录，初始化权限信息
    private void initializeAuthority(Long userId, LoginTypeEnum type, AuthorityDto leaderUserPosAuth) {
        boolean isTwitter = false;
        if (type != null && LoginTypeEnum.DEVELOP.equals(type) && leaderUserPosAuth != null) {
            // 用户是通过发展下级推客链接登录，判断上级权限，绑定上下级推客关系
            PosTwitterStatus twitterStatus = PosTwitterStatus.getEnum(leaderUserPosAuth.getTwitterStatus());
            AuthStatusEnum developStatus = AuthStatusEnum.getEnum(leaderUserPosAuth.getDevelop().byteValue());
            // 上级推客的推客权限和子权限发展下级推客权限都处于启用状态，则下级也处于相同的状态
            if (PosTwitterStatus.ENABLE.equals(twitterStatus) && AuthStatusEnum.ENABLE.equals(developStatus)) {
                isTwitter = true;
            }
        }

        initializeAuthority(userId, isTwitter);
    }

    private void initializeAuthority(Long userId, boolean isTwitter) {
        Authority authority = new Authority();
        authority.setUserId(userId);
        authority.setGet(AuthStatusEnum.ENABLE.getCode()); // 默认开启收款功能

        if (isTwitter) {
            authority.setGetRate(posConstants.getPosTwitterPoundageRate()); // 设置推客的收款功能权限默认费率
            authority.setTwitterStatus(PosTwitterStatus.ENABLE.getCode()); // 默认推客权限启用，限制发展下级客户和下级推客
            authority.setSpread(AuthStatusEnum.ENABLE.getCode()); // 默认发展下级客户权限启用
            authority.setDevelop(AuthStatusEnum.ENABLE.getCode()); // 默认发展下级推客权限启用
        } else {
            authority.setGetRate(posConstants.getPosPoundageRate()); // 设置收款功能权限默认费率
            authority.setTwitterStatus(PosTwitterStatus.DISABLE.getCode()); // 默认推客权限未启用，限制发展下级客户和下级推客
            authority.setSpread(AuthStatusEnum.DISABLE.getCode()); // 默认发展下级客户权限未启用
            authority.setDevelop(AuthStatusEnum.DISABLE.getCode()); // 默认发展下级推客权限未启用
        }
        authority.setAuditStatus(UserAuditStatus.NOT_SUBMIT.getCode()); // 默认身份认证状态未提交
        authorityDao.saveAuthority(authority);
    }

    private void initializeRelation(Long userId, LoginTypeEnum loginType, AuthorityDto leaderUserPosAuth) {
        PosTwitterStatus leaderTwitterStatus = PosTwitterStatus.getEnum(leaderUserPosAuth.getTwitterStatus());
        if (PosTwitterStatus.ENABLE.equals(leaderTwitterStatus)) {
            AuthStatusEnum spreadStatus = AuthStatusEnum.getEnum(leaderUserPosAuth.getSpread().byteValue());
            AuthStatusEnum developStatus = AuthStatusEnum.getEnum(leaderUserPosAuth.getDevelop().byteValue());
            if (LoginTypeEnum.SPREAD.equals(loginType) && AuthStatusEnum.ENABLE.equals(spreadStatus)) {
                twitterService.initializeTwitterCustomer(userId, leaderUserPosAuth.getUserId());
            } else if (LoginTypeEnum.DEVELOP.equals(loginType) && AuthStatusEnum.ENABLE.equals(developStatus)) {
                twitterService.initializeTwitter(userId, leaderUserPosAuth.getUserId());
            }
        }
    }


    @Override
    public ApiResult<NullObject> updateAuthority(Long authId, BaseAuthDto baseAuth, UserIdentifier user) {
        FieldChecker.checkEmpty(baseAuth, "baseAuth");
        FieldChecker.checkEmpty(user, "user");
        FieldChecker.checkMinValue(baseAuth.getGetRate(), posConstants.getPosPoundageRateDownLimit(), "getRate");

        AuthorityDetailDto oldAuth = authorityDao.findAuthDetailById(authId);
        if (oldAuth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (PosTwitterStatus.DISABLE.equals(oldAuth.parseTwitterStatus())
                && PosTwitterStatus.CLOSED.equals(baseAuth.parseTwitterStatus())) {
            // 特殊处理，未启用状态 -> 已关闭状态 视为没有操作更新推客状态
            baseAuth.setTwitterStatus(oldAuth.getTwitterStatus());
        }
        authorityDao.updateAuth(authId, baseAuth, user.getUserId());
        // 开通推客功能权限需要特殊处理，第一次开通推客功能需要新增推客记录
        if (PosTwitterStatus.ENABLE.equals(baseAuth.parseTwitterStatus())) {
            Twitter channel = posTwitterDao.getTwitterByUserId(oldAuth.getUserId());
            if (channel == null) {
                // 查询是否有关联上级推客
                TwitterCustomer junior = posTwitterDao.getTwitterCustomerByUserId(oldAuth.getUserId());
                if (junior != null && junior.getAvailable()) {
                    // 解除关联关系
                    junior.setAvailable(Boolean.FALSE);
                    junior.setUpdateUserId(user.getUserId());
                    posTwitterDao.updateTwitterCustomer(junior);
                }
                // 开通成为没有上级的推客
                channel = new Twitter();
                channel.setUserId(oldAuth.getUserId());
                posTwitterDao.saveTwitter(channel);
            }
        } else if (PosTwitterStatus.CLOSED.equals(baseAuth.parseTwitterStatus())) {
            Twitter twitter = posTwitterDao.getTwitterByUserId(oldAuth.getUserId());
            if (twitter != null) {
                // 解除推客的上下级推客推客关系，推客的上下级推客客户关系
                posTwitterDao.updateTwitterRelationAvailableByChild(twitter.getId(), false);
                posTwitterDao.updateTwitterRelationAvailableByParent(twitter.getId(), false);
                posTwitterDao.updateTwitterCustomerAvailableByTwitter(twitter.getId(), false);
            }
        }
        // 判断推客权限和收款费率是否变更，发送变更短信
        CustomerDto customer = customerService.findById(oldAuth.getUserId(), true);
        if (customer != null) {
            if (!oldAuth.parseTwitterStatus().equals(baseAuth.parseTwitterStatus())) {
                if (PosTwitterStatus.CLOSED.equals(baseAuth.parseTwitterStatus())) {
                    // 推客权限关闭
                    String closedMsg = posConstants.getPosTwitterStatusDisableTemplate();
                    smsService.sendMessage(customer.getPhone(), closedMsg);
                }
                if (PosTwitterStatus.ENABLE.equals(baseAuth.parseTwitterStatus())) {
                    // 推客权限开通
                    String enableMsg = posConstants.getPosTwitterStatusEnableTemplate();
                    smsService.sendMessage(customer.getPhone(), enableMsg);
                }
            }
            if (!oldAuth.getGetRate().equals(baseAuth.getGetRate())) {
                // 收款费率变更
                String getRateMsg = String.format(
                        posConstants.getPosUserGetRateChangedTemplate(),
                        baseAuth.getGetRate().multiply(new BigDecimal("100"), new MathContext(2)),
                        posConstants.getPosExtraPoundage());
                smsService.sendMessage(customer.getPhone(), getRateMsg);
            }
        }

        return ApiResult.succ();
    }

    @Override
    public ApiResult<BaseAuthDto> getBaseAuthById(Long posId) {
        FieldChecker.checkEmpty(posId, "posId");

        BaseAuthDto baseAuth = authorityDao.findBaseAuthById(posId);
        if (baseAuth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        baseAuth.setPoundage(posConstants.getPosExtraPoundage());

        return ApiResult.succ(baseAuth);
    }

    @Override
    public AuthorityDetailDto findAuthDetail(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        return authorityDao.findAuthDetail(userId);
    }

}
