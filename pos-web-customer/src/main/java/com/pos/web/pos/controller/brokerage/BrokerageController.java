/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.brokerage;

import com.google.common.collect.Lists;
import com.pos.basic.constant.OperationType;
import com.pos.basic.dto.operation.mq.OperationMsg;
import com.pos.basic.mq.MQReceiverType;
import com.pos.basic.service.OperationLogService;
import com.pos.common.util.basic.SegmentLocks;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.transaction.condition.orderby.PosTransactionOrderField;
import com.pos.transaction.condition.query.PosTransactionCondition;
import com.pos.transaction.constants.TransactionStatusType;
import com.pos.transaction.constants.TransactionType;
import com.pos.transaction.dto.brokerage.BrokerageDailyStatisticsDto;
import com.pos.transaction.dto.brokerage.BrokerageGeneralInfoDto;
import com.pos.transaction.dto.transaction.TransactionRecordDto;
import com.pos.transaction.service.CustomerBrokerageService;
import com.pos.transaction.service.PosUserTransactionRecordService;
import com.pos.user.session.UserInfo;
import com.pos.web.pos.converter.PosConverter;
import com.pos.web.pos.vo.brokerage.BrokerageAppliedRecordVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 佣金相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
@RestController
@RequestMapping("/brokerage")
@Api(value = "/brokerage", description = "v2.0.0 * 佣金相关接口")
public class BrokerageController {

    private final static Logger LOG = LoggerFactory.getLogger(BrokerageController.class);

    private final static SegmentLocks SEG_LOCKS = new SegmentLocks(32, false);

    @Resource
    private CustomerBrokerageService customerBrokerageService;

    @Resource
    private PosUserTransactionRecordService posUserTransactionRecordService;

    @Resource
    private OperationLogService operationLogService;

    @RequestMapping(value = "general", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取佣金简要统计信息", notes = "获取佣金简要统计信息")
    public ApiResult<BrokerageGeneralInfoDto> getTwitterGeneralInfo(
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(customerBrokerageService.getBrokerageGeneral(userInfo.getId()));
    }

    @RequestMapping(value = "daily", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取每日收益记录", notes = "获取每日收益记录")
    public ApiResult<List<BrokerageDailyStatisticsDto>> queryDailyStatistics(
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize,
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(customerBrokerageService.queryDailyBrokerage(
                userInfo.getId(), LimitHelper.create(pageNum, pageSize), true));
    }

    @RequestMapping(value = "apply", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 申请提现", notes = "申请提现，不传金额，后端计算，申请提交成功返回申请提现的金额")
    public ApiResult<BigDecimal> addWithdrawDepositApply(
            @FromSession UserInfo userInfo) {
        // 敏感操作，记录操作日志
        OperationMsg msg = OperationMsg.create(userInfo.buildUserIdentifier(), OperationType.BrokerageWithdraw.发起佣金提现);

        ApiResult<BigDecimal> result;
        // 敏感操作，加锁
        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(userInfo.getId());
        try {
            hasLock = lock.tryLock(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("用户{}发起佣金提现时尝试获取锁失败！", userInfo.getId());
        }
        try {
            if (hasLock) {
                // 执行敏感操作
                result = customerBrokerageService.withdrawBrokerage(userInfo.getId());
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

    @RequestMapping(value = "applied/record", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 提现记录", notes = "提现记录")
    public ApiResult<Pagination<BrokerageAppliedRecordVo>> queryWithdrawDepositRecord(
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize,
            @FromSession UserInfo userInfo) {
        LimitHelper limitHelper = new LimitHelper(pageNum, pageSize);
        PosTransactionCondition condition = new PosTransactionCondition();
        condition.setUserId(userInfo.getId());
        condition.setExcludedStatuses(Lists.newArrayList(
                TransactionStatusType.ORIGIN_TRANSACTION.getCode(),
                TransactionStatusType.PREDICT_TRANSACTION.getCode()));
        condition.setTransactionType(TransactionType.BROKERAGE_WITHDRAW.getCode());

        Pagination<TransactionRecordDto> pagination = posUserTransactionRecordService.queryUserTransactionRecord(
                condition, PosTransactionOrderField.getPayTimeOrderHelper(), limitHelper).getData();
        Pagination<BrokerageAppliedRecordVo> result = Pagination.newInstance(limitHelper, pagination.getTotalCount());

        if (pagination.getTotalCount() > 0 && !CollectionUtils.isEmpty(pagination.getResult())) {
            List<BrokerageAppliedRecordVo> dataList = pagination.getResult().stream().map(
                    PosConverter::toBrokerageAppliedRecordVo).collect(Collectors.toList());
            result.setResult(dataList);
        }

        return ApiResult.succ(result);
    }
}
