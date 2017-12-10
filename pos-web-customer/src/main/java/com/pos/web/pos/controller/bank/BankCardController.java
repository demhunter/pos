/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.bank;

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
import com.pos.transaction.dto.card.PosCardDto;
import com.pos.transaction.dto.request.BindCardDto;
import com.pos.transaction.service.PosCardService;
import com.pos.user.session.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 银行卡管理Controller
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@RestController
@RequestMapping("/bank/card")
@Api(value = "/bank/card", description = "v2.0.0 * 银行卡管理相关接口")
public class BankCardController {

    private final static Logger LOG = LoggerFactory.getLogger(BankCardController.class);

    private final static SegmentLocks SEG_LOCKS = new SegmentLocks(32, false);

    @Resource
    private PosCardService posCardService;

    @Resource
    private OperationLogService operationLogService;

    @RequestMapping(value = "settlement", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取当前绑定的结算银行卡信息", notes = "获取当前绑定的结算银行卡信息")
    public ApiResult<PosCardDto> getSettlementBankCard(
            @FromSession UserInfo userInfo) {
        return posCardService.findWithdrawCard(userInfo.getId());
    }

    @RequestMapping(value = "settlement", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 更换当前绑定的结算银行卡", notes = "更换当前绑定的结算银行卡")
    public ApiResult<NullObject> updateSettlementBankCard(
            @ApiParam(name = "bindCardInfo", value = "绑卡信息")
            @RequestBody BindCardDto bindCardInfo,
            @FromSession UserInfo userInfo) {
        // 敏感操作，记录操作日志
        OperationMsg msg = OperationMsg.create(userInfo.buildUserIdentifier(), OperationType.Certification.更换结算银行卡);
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
            LOG.error("用户{}更换结算银行卡时尝试获取锁失败！", userInfo.getId());
        }
        try {
            if (hasLock) {
                // 执行敏感操作
                result = posCardService.alterWithdrawCard(userInfo.getId(), bindCardInfo);
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

    @RequestMapping(value = "expenditure", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取当前保存的已使用银行卡列表", notes = "获取当前保存的已使用银行卡列表")
    public ApiResult<List<PosCardDto>> getExpenditureCards(
            @FromSession UserInfo userInfo) {
        return posCardService.queryOutBankCard(userInfo.getId());
    }

    @RequestMapping(value = "expenditure/{cardId}/delete", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 删除已保存的卡信息", notes = "删除已保存的卡信息")
    public ApiResult<NullObject> deleteBankCard(
            @ApiParam(name = "cardId", value = "银行卡id")
            @PathVariable("cardId") Long cardId,
            @FromSession UserInfo userInfo) {
        return posCardService.deleteOutBankCard(cardId, userInfo.getId());
    }
}
