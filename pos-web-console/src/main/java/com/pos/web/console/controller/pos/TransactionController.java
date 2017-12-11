/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.pos;

import com.pos.basic.constant.OperationType;
import com.pos.basic.dto.operation.mq.OperationMsg;
import com.pos.basic.mq.MQReceiverType;
import com.pos.basic.service.OperationLogService;
import com.pos.common.util.basic.SegmentLocks;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.mvc.view.XlsStyle;
import com.pos.common.util.mvc.view.XlsView;
import com.pos.common.util.validation.FieldChecker;
import com.pos.transaction.condition.orderby.PosTransactionOrderField;
import com.pos.transaction.condition.query.PosTransactionCondition;
import com.pos.transaction.constants.TransactionType;
import com.pos.transaction.dto.TransactionEnumsDto;
import com.pos.transaction.dto.failure.TransactionFailureRecordDto;
import com.pos.transaction.dto.transaction.TransactionHandledInfoDto;
import com.pos.transaction.dto.transaction.TransactionRecordDto;
import com.pos.transaction.service.PosService;
import com.pos.transaction.service.PosUserTransactionRecordService;
import com.pos.user.service.UserService;
import com.pos.user.session.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 交易记录相关Controller
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
@RestController
@RequestMapping("/transaction")
@Api(value = "/transaction", description = "v2.0.0 交易记录相关接口(接口路径有变化，加入佣金交易查询)")
public class TransactionController {

    private final static Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final static SegmentLocks SEG_LOCKS = new SegmentLocks(8, false);

    @Resource
    private PosUserTransactionRecordService posUserTransactionRecordService;

    @Resource
    private UserService userService;

    @Resource
    private PosService posService;

    @Resource
    private OperationLogService operationLogService;

    @RequestMapping(value = "enum", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 交易相关枚举查询", notes = "交易相关枚举查询")
    public ApiResult<TransactionEnumsDto> findTransactionEnum() {
        return ApiResult.succ(TransactionEnumsDto.getInstance());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 查询交易列表", notes = "查询交易列表（v2.0.0 新增交易失败次数，根据交易类型查询，佣金列表页查询此接口）")
    public ApiResult<Pagination<TransactionRecordDto>> queryTransactionRecords(
            @ApiParam(name = "transactionType", value = "v2.0.0 * 交易类型（1：客户提现交易；2：佣金提现交易；3：客户等级晋升手续费支付交易；默认为1：客户提现交易）")
            @RequestParam(name = "transactionType", required = false, defaultValue = "1") Integer transactionType,
            @ApiParam(name = "transactionStatus", value = "到账状态：-1 = 已创建，0 = 已下单，1 = 交易处理中，2 = 交易失败，3 = 交易成功，4 = 已手动处理（v2.0.0 * 调用枚举接口，获取状态枚举）")
            @RequestParam(name = "transactionStatus", required = false) Integer transactionStatus,
            @ApiParam(name = "beginTime", value = "交易开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "交易结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam(name = "searchKey", value = "搜索关键字（姓名）")
            @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize) {
        LimitHelper limitHelper = LimitHelper.create(pageNum, pageSize);
        PosTransactionCondition condition = new PosTransactionCondition();
        condition.setTransactionType(transactionType);
        condition.setStatus(transactionStatus);
        condition.parseBeginAndEndTime(beginTime, endTime);
        if (StringUtils.isNotEmpty(searchKey)) {
            List<Long> includeUserIds = userService.queryUserIds(searchKey);
            if (CollectionUtils.isEmpty(includeUserIds)) {
                Pagination<TransactionRecordDto> pagination = Pagination.newInstance(limitHelper, 0);
                return ApiResult.succ(pagination);
            }
            condition.setIncludeUserIds(includeUserIds);
        }

        return posUserTransactionRecordService.queryUserTransactionRecord(
                condition, PosTransactionOrderField.getDefaultOrderHelper(), limitHelper);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 导出交易列表", notes = "导出交易列表（ps：该接口前端不处理返回结果，导出成功后浏览器将自动下载Excel文件）")
    public ModelAndView queryAndExportTransactionRecords(
            @ApiParam(name = "transactionType", value = "v2.0.0 * 交易类型（1：客户提现交易；2：佣金提现交易；3：客户等级晋升手续费支付交易；默认为1：客户提现交易）")
            @RequestParam(name = "transactionType", defaultValue = "1") Integer transactionType,
            @ApiParam(name = "transactionStatus", value = "到账状态：-1 = 已创建，0 = 已下单，1 = 交易处理中，2 = 交易失败，3 = 交易成功，4 = 已手动处理（v2.0.0 * 调用枚举接口，获取状态枚举）")
            @RequestParam(name = "transactionStatus", required = false) Integer transactionStatus,
            @ApiParam(name = "beginTime", value = "交易开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "交易结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam(name = "searchKey", value = "搜索关键字（手机号/姓名）")
            @RequestParam(name = "searchKey", required = false) String searchKey) {
        XlsView xlsView;
        LimitHelper limitHelper = LimitHelper.create(1, Integer.MAX_VALUE, false);
        TransactionType type = TransactionType.getEnum(transactionType);

        PosTransactionCondition condition = new PosTransactionCondition();
        condition.setTransactionType(transactionType);
        condition.setStatus(transactionStatus);
        condition.parseBeginAndEndTime(beginTime, endTime);
        if (StringUtils.isNotEmpty(searchKey)) {
            List<Long> includeUserIds = userService.queryUserIds(searchKey);
            if (CollectionUtils.isEmpty(includeUserIds)) {
                xlsView = new XlsView(0, getRawNames(type))
                        .setXlsStyle(new XlsStyle().setSheetName(getSheetName(type)).setColumnWidth(17));
                return new ModelAndView(xlsView);
            }
            condition.setIncludeUserIds(includeUserIds);
        }
        List<TransactionRecordDto> result = posUserTransactionRecordService.queryUserTransactionRecord(
                condition, PosTransactionOrderField.getDefaultOrderHelper(), limitHelper).getData().getResult();
        xlsView = new XlsView(CollectionUtils.isEmpty(result) ? 0 : result.size(), getRawNames(type))
                .setXlsStyle(new XlsStyle().setSheetName(getSheetName(type)).setColumnWidth(17));
        if (!CollectionUtils.isEmpty(result)) {
            result.forEach(transaction -> xlsView.addRowValues(getRawValues(type, transaction)));
        }

        return new ModelAndView(xlsView);
    }

    private String[] getRawNames(TransactionType type) {
        if (TransactionType.BROKERAGE_WITHDRAW.equals(type)) {
            return new String[]{
                    "ID", "订单号", "提现人", "手机号码", "收款银行卡信息", "提现金额",
                    "提现时间", "成功时间", "提现状态", "失败提示次数"};
        } else {
            return new String[]{
                    "ID", "订单号", "收款人", "手机号码", "收款银行卡信息", "收款金额",
                    "到账金额", "下单时间", "支付时间", "成功时间", "交易状态", "失败提示次数"};
        }
    }

    private String getSheetName(TransactionType type) {
        if (TransactionType.BROKERAGE_WITHDRAW.equals(type)) {
            return "佣金提现列表";
        } else {
            return "快捷收款交易列表";
        }
    }

    private Object[] getRawValues(TransactionType type, TransactionRecordDto transaction) {
        if (TransactionType.BROKERAGE_WITHDRAW.equals(type)) {
            return new Object[]{
                    transaction.getId(),
                    transaction.getRecordNum(),
                    transaction.getUserName(),
                    transaction.getUserPhone(),
                    transaction.getInCardInfo() == null ? "" : transaction.getInCardInfo().getBankName() + "（" + transaction.getInCardInfo().getCardNO() + "）",
                    transaction.getAmount(),
                    SimpleDateUtils.formatDate(transaction.getCreateDate(), SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()),
                    transaction.getCompleteDate() == null ? "" : SimpleDateUtils.formatDate(transaction.getCompleteDate(), SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()),
                    transaction.getStatusDesc(),
                    transaction.getFailureTimes()
            };
        } else {
            return new Object[]{
                    transaction.getId(),
                    transaction.getRecordNum(),
                    transaction.getUserName(),
                    transaction.getUserPhone(),
                    transaction.getInCardInfo() == null ? "" : transaction.getInCardInfo().getBankName() + "（" + transaction.getInCardInfo().getCardNO() + "）",
                    transaction.getAmount(),
                    transaction.getArrivalAmount(),
                    SimpleDateUtils.formatDate(transaction.getCreateDate(), SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()),
                    transaction.getPayDate() == null ? "" : SimpleDateUtils.formatDate(transaction.getPayDate(), SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()),
                    transaction.getCompleteDate() == null ? "" : SimpleDateUtils.formatDate(transaction.getCompleteDate(), SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()),
                    transaction.getStatusDesc(),
                    transaction.getFailureTimes()
            };
        }
    }

    @RequestMapping(value = "{recordId}/withdraw/again", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 重新发起结算", notes = "重新发起结算")
    public ApiResult<NullObject> repeatSendWithdrawRequest(
            @ApiParam(name = "recordId", value = "交易记录id")
            @PathVariable("recordId") Long recordId,
            @FromSession UserInfo userInfo) {
        // 敏感操作，记录操作日志
        OperationMsg msg = OperationMsg.create(userInfo.buildUserIdentifier(), OperationType.EPOS.失败交易重发);
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("recordId", recordId);
        msg.addFailureRequestInfo(requestMap);

        ApiResult<NullObject> result;
        // 敏感操作，加锁
        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(recordId);
        try {
            hasLock = lock.tryLock(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("用户{}重新发起结算时尝试获取锁失败！recordId = {}", userInfo.getId(), recordId);
        }
        try {
            if (hasLock) {
                // 执行敏感操作
                result = posService.againPayToPosUserForFailed(recordId, userInfo.buildUserIdentifier());
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
            operationLogService.sendOperationMsg(msg, MQReceiverType.POS_CONSOLE);
        }
        return result;
    }

    @RequestMapping(value = "{recordId}/withdraw/handled", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 手动处理结算", notes = "手动处理结算")
    public ApiResult<NullObject> handledTransaction(
            @ApiParam(name = "recordId", value = "交易记录id")
            @PathVariable("recordId") Long recordId,
            @ApiParam(name = "handledInfo", value = "手动处理结算信息")
            @RequestBody TransactionHandledInfoDto handledInfo,
            @FromSession UserInfo userInfo) {
        // 敏感操作，记录操作日志
        OperationMsg msg = OperationMsg.create(userInfo.buildUserIdentifier(), OperationType.EPOS.失败交易手动处理);
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("recordId", recordId);
        requestMap.put("RequestBody", handledInfo);
        msg.addFailureRequestInfo(requestMap);

        ApiResult<NullObject> result;
        // 敏感操作，加锁
        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(recordId);
        try {
            hasLock = lock.tryLock(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("用户{}失败交易手动处理时尝试获取锁失败！recordId = {}", userInfo.getId(), recordId);
        }
        try {
            if (hasLock) {
                // 执行敏感操作
                result = posService.handledTransaction(recordId, handledInfo, userInfo.buildUserIdentifier());
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
            operationLogService.sendOperationMsg(msg, MQReceiverType.POS_CONSOLE);
        }
        return result;
    }

    @RequestMapping(value = "{recordId}/withdraw/handled", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 获取手动处理结算的处理信息", notes = "获取手动处理结算的处理信息")
    public ApiResult<TransactionHandledInfoDto> getTransactionHandledInfo(
            @ApiParam(name = "recordId", value = "交易记录id")
            @PathVariable("recordId") Long recordId) {
        return posService.getHandledTransactionInfo(recordId);
    }

    @RequestMapping(value = "{recordId}/failures", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 查询指定交易的失败记录", notes = "查询指定交易的失败记录")
    public ApiResult<List<TransactionFailureRecordDto>> getTransaction(
            @ApiParam(name = "recordId", value = "交易id")
            @PathVariable("recordId") Long recordId) {
        return posUserTransactionRecordService.queryFailureRecords(recordId);
    }
}
