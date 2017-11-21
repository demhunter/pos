/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.posconsole.controller.pos;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.pos.common.util.basic.SegmentLocks;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.mvc.view.XlsStyle;
import com.pos.common.util.mvc.view.XlsView;
import com.pos.pos.condition.orderby.PosTransactionOrderField;
import com.pos.pos.condition.query.PosTransactionCondition;
import com.pos.pos.dto.transaction.TransactionHandledInfoDto;
import com.pos.pos.dto.transaction.TransactionRecordDto;
import com.pos.pos.service.PosService;
import com.pos.pos.service.PosUserTransactionRecordService;
import com.pos.user.service.UserService;
import com.pos.user.session.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 快捷收款交易记录相关Controller
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
@RestController
@RequestMapping("/pos/transaction")
@Api(value = "/pos/transaction", description = "v1.0.0 * 快捷收款交易记录相关接口")
public class PosTransactionController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PosTransactionController.class);

    private final static SegmentLocks SEG_LOCKS = new SegmentLocks(8, false);

    @Resource
    private PosUserTransactionRecordService posUserTransactionRecordService;

    @Resource
    private UserService userService;

    @Resource
    private PosService posService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 查询交易列表", notes = "wb * 查询交易列表")
    public ApiResult<Pagination<TransactionRecordDto>> queryTransactionRecords(
            @ApiParam(name = "transactionStatus", value = "到账状态：0 = 已下单，1 = 交易处理中，2 = 交易失败，3 = 交易成功，4 = 已手动处理")
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
    @ApiOperation(value = "v1.0.0 * 导出交易列表", notes = "wb * 导出交易列表（ps：该接口前端不处理返回结果，导出成功后浏览器将自动下载Excel文件）")
    public ModelAndView queryAndExportTransactionRecords(
            @ApiParam(name = "transactionStatus", value = "到账状态：0 = 失败，1 = 成功，2 = 处理中，3 = 已手动处理")
            @RequestParam(name = "transactionStatus", required = false) Integer transactionStatus,
            @ApiParam(name = "beginTime", value = "交易开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "交易结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam(name = "searchKey", value = "搜索关键字（手机号/姓名）")
            @RequestParam(name = "searchKey", required = false) String searchKey) {
        XlsView xlsView;
        LimitHelper limitHelper = LimitHelper.create(1, Integer.MAX_VALUE, false);
        PosTransactionCondition condition = new PosTransactionCondition();
        condition.setStatus(transactionStatus);
        condition.parseBeginAndEndTime(beginTime, endTime);
        if (StringUtils.isNotEmpty(searchKey)) {
            List<Long> includeUserIds = userService.queryUserIds(searchKey);
            if (CollectionUtils.isEmpty(includeUserIds)) {
                xlsView = new XlsView(0, new String[]
                        {"ID", "订单号", "收款人", "手机号码", "收款银行卡信息", "收款金额",
                                "到账金额", "交易时间", "成功时间", "到账状态"})
                        .setXlsStyle(new XlsStyle().setSheetName("快捷收款交易列表").setColumnWidth(17));
                return new ModelAndView(xlsView);
            }
            condition.setIncludeUserIds(includeUserIds);
        }
        List<TransactionRecordDto> result = posUserTransactionRecordService.queryUserTransactionRecord(
                condition, PosTransactionOrderField.getDefaultOrderHelper(), limitHelper).getData().getResult();
        xlsView = new XlsView(CollectionUtils.isEmpty(result) ? 0 : result.size(), new String[]
                {"ID", "订单号", "收款人", "手机号码", "收款银行卡信息", "收款金额",
                        "到账金额", "交易时间", "成功时间", "到账状态"})
                .setXlsStyle(new XlsStyle().setSheetName("快捷收款交易列表").setColumnWidth(17));
        if (!CollectionUtils.isEmpty(result)) {
            result.forEach(transaction ->
                xlsView.addRowValues(new Object[]{
                        transaction.getId(),
                        transaction.getRecordNum(),
                        transaction.getUserName(),
                        transaction.getUserPhone(),
                        transaction.getInCardInfo() == null ? "" : transaction.getInCardInfo().getBankName() + "（" + transaction.getInCardInfo().getCardNO() + "）",
                        transaction.getAmount(),
                        transaction.getArrivalAmount(),
                        SimpleDateUtils.formatDate(transaction.getPayDate(), SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()),
                        transaction.getCompleteDate() == null ? "" : SimpleDateUtils.formatDate(transaction.getCompleteDate(), SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()),
                        transaction.getStatusDesc()
                }));
        }

        return new ModelAndView(xlsView);
    }

    @RequestMapping(value = "{recordId}/withdraw/again", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 重新发起结算", notes = "重新发起结算")
    public ApiResult<NullObject> repeatSendWithdrawRequest(
            @ApiParam(name = "recordId", value = "交易记录id")
            @PathVariable("recordId") Long recordId,
            @FromSession UserInfo userInfo) {
        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(recordId);
        try {
            hasLock = lock.tryLock(8L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("重新发起结算时尝试获取锁失败！recordId = " + recordId, e);
        }
        if (hasLock) {
            try {
                return posService.againPayToPosUserForFailed(recordId, userInfo.buildUserIdentifier());
            } finally {
                lock.unlock();
            }
        } else {
            return ApiResult.fail(CommonErrorCode.ACCESS_TIMEOUT);
        }
    }

    @RequestMapping(value = "{recordId}/withdraw/handled", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 手动处理结算", notes = "手动处理结算")
    public ApiResult<NullObject> handledTransaction(
            @ApiParam(name = "recordId", value = "交易记录id")
            @PathVariable("recordId") Long recordId,
            @ApiParam(name = "handledInfo", value = "手动处理结算信息")
            @RequestBody TransactionHandledInfoDto handledInfo,
            @FromSession UserInfo userInfo) {
        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(recordId);
        try {
            hasLock = lock.tryLock(8L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("手动处理结算时尝试获取锁失败！recordId = " + recordId, e);
        }
        if (hasLock) {
            try {
                return posService.handledTransaction(recordId, handledInfo, userInfo.buildUserIdentifier());
            } finally {
                lock.unlock();
            }
        } else {
            return ApiResult.fail(CommonErrorCode.ACCESS_TIMEOUT);
        }
    }

    @RequestMapping(value = "{recordId}/withdraw/handled", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 获取手动处理结算的处理信息", notes = "获取手动处理结算的处理信息")
    public ApiResult<TransactionHandledInfoDto> getTransactionHandledInfo(
            @ApiParam(name = "recordId", value = "交易记录id")
            @PathVariable("recordId") Long recordId) {
        return posService.getHandledTransactionInfo(recordId);
    }
}
