/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.authentication;

import com.pos.authority.dto.identity.CustomerIdentityDto;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.basic.constant.OperationType;
import com.pos.basic.dto.operation.mq.OperationMsg;
import com.pos.basic.mq.MQReceiverType;
import com.pos.basic.service.OperationLogService;
import com.pos.common.util.basic.SegmentLocks;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.transaction.dto.request.BindCardDto;
import com.pos.transaction.service.PosCardService;
import com.pos.user.session.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 身份认证和结算卡绑定相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@RestController
@RequestMapping("/authentication")
@Api(value = "/authentication", description = "v2.0.0 * 身份认证和结算卡绑定相关接口")
public class AuthenticationController {

    private final static Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    private final static SegmentLocks SEG_LOCKS = new SegmentLocks(32, false);

    @Resource
    private CustomerAuthorityService customerAuthorityService;

    @Resource
    private PosCardService posCardService;

    @Resource
    private OperationLogService operationLogService;

    @RequestMapping(value = "identity", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取已提交的身份认证信息", notes = "获取已提交的身份认证信息（v2.0.0 不传持证照）")
    public ApiResult<CustomerIdentityDto> getIdentityInfo(
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(customerAuthorityService.getCustomerIdentity(userInfo.getId()));
    }

    @RequestMapping(value = "identity", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 提交身份认证信息-1", notes = "提交身份认证信息-1（v2.0.0 不传持证照）")
    public ApiResult<NullObject> updateIdentityInfo(
            @ApiParam(name = "identityInfo", value = "身份认证信息")
            @RequestBody CustomerIdentityDto identityInfo,
            @FromSession UserInfo userInfo) {
        return customerAuthorityService.updateCustomerIdentity(userInfo.getId(), identityInfo);
    }

    @RequestMapping(value = "bank-card", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取绑定的收款卡信息，身份认证信息-2", notes = "获取绑定的收款卡信息，身份认证信息-2（PS：从未绑定过收款银行卡则返回空）")
    public ApiResult<BindCardDto> getBindCardInfo(
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(posCardService.getWithdrawCard(userInfo.getId()));
    }

    @RequestMapping(value = "bank-card", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 绑定收款卡，提交身份认证信息-2", notes = "绑定收款银行卡，提交身份认证信息-2")
    public ApiResult<NullObject> bingCard(
            @ApiParam(name = "bindCardInfo", value = "绑卡信息")
            @RequestBody BindCardDto bindCardInfo,
            @FromSession UserInfo userInfo) {
        // 敏感操作，记录操作日志
        OperationMsg msg = OperationMsg.create(userInfo.buildUserIdentifier(), OperationType.Certification.绑定结算银行卡);
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("RequestBody", bindCardInfo);
        msg.addFailureRequestInfo(requestMap);

        ApiResult<NullObject> result;
        // 敏感操作，加锁
        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(userInfo.getId());
        try {
            hasLock = lock.tryLock(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("用户{}绑定结算银行卡时尝试获取锁失败！", userInfo.getId());
        }
        try {
            if (hasLock) {
                // 执行敏感操作
                result = posCardService.bindWithdrawCard(userInfo.getId(), bindCardInfo);
                if (!result.isSucc()) {
                    msg.operateFailure();
                    msg.setFailReason(result.getMessage());
                } else {
                    msg.operateSuccess();
                }
            } else {
                result = ApiResult.fail(CommonErrorCode.ACCESS_TIMEOUT);
                msg.operateFailure();
                msg.setFailReason("超时错误");
            }
        } catch (ValidationException validationException) {
            msg.operateFailure();
            msg.setFailReason("参数错误");
            msg.setException(validationException);
            throw validationException;
        } catch (Exception e) {
            msg.operateFailure();
            msg.setFailReason("服务器内部错误");
            msg.setException(e);
            throw e;
        } finally {
            if (hasLock) {
                lock.unlock();
            }
            // 发送操作消息
            operationLogService.sendOperationMsg(msg, MQReceiverType.POS_CUSTOMER);
        }

        return result;
    }
}
