/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.pos;

import com.google.common.collect.Lists;
import com.pos.common.util.basic.SegmentLocks;
import com.pos.common.util.constans.GlobalConstants;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.web.http.HttpClientUtils;
import com.pos.common.util.web.http.HttpRequestUtils;
import com.pos.transaction.condition.orderby.PosTransactionOrderField;
import com.pos.transaction.condition.query.PosTransactionCondition;
import com.pos.transaction.constants.TransactionStatusType;
import com.pos.transaction.constants.TransactionType;
import com.pos.transaction.dto.CreateOrderDto;
import com.pos.transaction.dto.GetSignDto;
import com.pos.transaction.dto.get.QuickGetMoneyDto;
import com.pos.transaction.dto.request.GetMoneyDto;
import com.pos.transaction.dto.transaction.SelectCardRequestDto;
import com.pos.transaction.dto.transaction.TransactionRecordDto;
import com.pos.transaction.helipay.vo.ConfirmPayResponseVo;
import com.pos.transaction.service.PosService;
import com.pos.transaction.service.PosUserTransactionRecordService;
import com.pos.user.session.UserInfo;
import com.pos.web.pos.converter.PosConverter;
import com.pos.web.pos.vo.request.GetSignRequestDto;
import com.pos.web.pos.vo.response.OnlyStringVo;
import com.pos.web.pos.vo.response.RecordVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 支付业务相关接口
 *
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
@RestController
@RequestMapping("/pos")
@Api(value = "/pos", description = "v1.0.0 * 支付业务相关接口")
public class PosController {

    private final static Logger LOG = LoggerFactory.getLogger(PosController.class);

    private final static SegmentLocks SEG_LOCKS = new SegmentLocks(32, false);

    @Resource
    private GlobalConstants globalConstants;

    @Resource
    private PosService posService;

    @Resource
    private PosUserTransactionRecordService posUserTransactionRecordService;


    @RequestMapping(value = "explain", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 功能说明", notes = "功能说明的地址")
    public ApiResult<OnlyStringVo> explain() {
        OnlyStringVo onlyStringVo = new OnlyStringVo();
        onlyStringVo.setResult(globalConstants.explainUrl);
        return ApiResult.succ(onlyStringVo);
    }

    @RequestMapping(value = "quickGetMoney", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 快捷收款-1，获取当前用户快捷收款相关信息", notes = "点击快捷收款，快捷收款-1，获取当前用户快捷收款相关信息" +
            "(v2.0.0 已使用的银行卡从银行卡相关接口获取)")
    public ApiResult<QuickGetMoneyDto> quickGetMoney(
            @FromSession UserInfo userInfo) {
        return posService.getQuickInfo(userInfo.getId());
    }

    @RequestMapping(value = "selectCard", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 快捷收款-2-1，选择一张卡，下单", notes = "快捷收款-2-1，选择一张付款的卡，并且填写金额,下单")
    public ApiResult<CreateOrderDto> selectCard(
            @ApiParam(name = "selectCardRequestDto", value = "选卡下单信息")
            @RequestBody SelectCardRequestDto selectCardRequestDto,
            @FromSession UserInfo userInfo, HttpServletRequest request) {
        String ip = HttpRequestUtils.getRealRemoteAddr(request);
        // TODO 记录操作日志
        return posService.selectCreateRecord(userInfo.getId(), selectCardRequestDto ,ip);
    }

    @RequestMapping(value = "writeCard", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 快捷收款-2-2，填写卡信息，下单", notes = "快捷收款-2-2，手动填写卡信息，下单")
    public ApiResult<CreateOrderDto> writeCard(
            @ApiParam(name = "getMoneyRequestDto", value = "填写卡信息下单信息")
            @RequestBody GetMoneyDto getMoneyRequestDto,
            @FromSession UserInfo userInfo, HttpServletRequest request) {
        String ip = HttpRequestUtils.getRealRemoteAddr(request);
        // TODO 记录操作日志
        return posService.writeCreateRecord(getMoneyRequestDto,  userInfo.getId(), ip);
    }

    @RequestMapping(value = "getSmsCode/{recordId}", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 快捷收款-3，提现时获取验证码", notes = "提现时获取验证码")
    public ApiResult<NullObject> getSmsCode(
            @ApiParam(name = "recordId", value = "记录ID")
            @PathVariable("recordId") Long recordId,
            @FromSession UserInfo userInfo) {
        // TODO 记录操作日志
        return posService.sendPayValidateSmsCode(userInfo.getId(), recordId);
    }

    @RequestMapping(value = "getMoney/{recordId}", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 快捷收款-4，提现", notes = "快捷收款-3，提现")
    public ApiResult<NullObject> getMoney(
            @ApiParam(name = "recordId", value = "记录ID")
            @PathVariable("recordId") Long recordId,
            @ApiParam(name = "smsCode", value = "短信验证码")
            @RequestParam("smsCode") String smsCode,
            @FromSession UserInfo userInfo, HttpServletRequest request) {
        // TODO 记录操作日志
        String ip = HttpRequestUtils.getRealRemoteAddr(request);
        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(recordId);
        try {
            hasLock = lock.tryLock(8L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("快捷收款提现时尝试获取锁失败！recordId = " + recordId, e);
        }
        if (hasLock) {
            try {
                return posService.confirmPay(userInfo.getId(), smsCode, recordId, ip);
            } finally {
                lock.unlock();
            }
        } else {
            return ApiResult.fail(CommonErrorCode.ACCESS_TIMEOUT);
        }
    }

    @RequestMapping(value = "records", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 收款记录", notes = "收款记录（* 卡号只有后四位）")
    public ApiResult<List<RecordVo>> transactionRecords(
            @ApiParam(name = "pageNum", value = "页码")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页大小")
            @RequestParam("pageSize") int pageSize,
            @FromSession UserInfo userInfo) {
        LimitHelper limitHelper = new LimitHelper(pageNum, pageSize);
        PosTransactionCondition condition = new PosTransactionCondition();
        condition.setUserId(userInfo.getId());
        condition.setExcludedStatuses(Lists.newArrayList(
                TransactionStatusType.ORIGIN_TRANSACTION.getCode(),
                TransactionStatusType.PREDICT_TRANSACTION.getCode()));
        condition.setTransactionType(TransactionType.NORMAL_WITHDRAW.getCode());

        List<TransactionRecordDto> records = posUserTransactionRecordService.queryUserTransactionRecord(
                condition, PosTransactionOrderField.getPayTimeOrderHelper(), limitHelper).getData().getResult();
        if (CollectionUtils.isEmpty(records)) {
            return ApiResult.succ();
        }
        Map<String, String> logoMap = posService.getAllBankLogo();
        List<RecordVo> result = records.stream().map(record ->
                PosConverter.toRecordVo(record, logoMap)).collect(Collectors.toList());
        return ApiResult.succ(result);
    }

    //合利宝的回调
    @RequestMapping(value = "payCallback", method = RequestMethod.POST)
    public void posCallback(
            @RequestParam(name = "rt1_bizType", required = false)String rt1_bizType,
            @RequestParam(name = "rt2_retCode", required = false)String rt2_retCode,
            @RequestParam(name = "rt3_retMsg", required = false)String rt3_retMsg,
            @RequestParam(name = "rt4_customerNumber", required = false)String rt4_customerNumber,
            @RequestParam(name = "rt5_orderId", required = false)String rt5_orderId,
            @RequestParam(name = "rt6_serialNumber", required = false)String rt6_serialNumber,
            @RequestParam(name = "rt7_completeDate", required = false)String rt7_completeDate,
            @RequestParam(name = "rt8_orderAmount", required = false)String rt8_orderAmount,
            @RequestParam(name = "rt9_orderStatus", required = false)String rt9_orderStatus,
            @RequestParam(name = "rt10_bindId", required = false)String rt10_bindId,
            @RequestParam(name = "rt11_bankId", required = false)String rt11_bankId,
            @RequestParam(name = "rt12_onlineCardType", required = false)String rt12_onlineCardType,
            @RequestParam(name = "rt13_cardAfterFour", required = false)String rt13_cardAfterFour,
            @RequestParam(name = "rt14_userId", required = false)String rt14_userId,
            @RequestParam(name = "sign", required = false)String sign,
            HttpServletResponse response) {
        ConfirmPayResponseVo confirmPayResponseVo = new ConfirmPayResponseVo();
        confirmPayResponseVo.setRt1_bizType(rt1_bizType);
        confirmPayResponseVo.setRt2_retCode(rt2_retCode);
        confirmPayResponseVo.setRt3_retMsg(rt3_retMsg);
        confirmPayResponseVo.setRt4_customerNumber(rt4_customerNumber);
        confirmPayResponseVo.setRt5_orderId(rt5_orderId);
        confirmPayResponseVo.setRt6_serialNumber(rt6_serialNumber);
        confirmPayResponseVo.setRt7_completeDate(rt7_completeDate);
        confirmPayResponseVo.setRt8_orderAmount(rt8_orderAmount);
        confirmPayResponseVo.setRt9_orderStatus(rt9_orderStatus);
        confirmPayResponseVo.setRt10_bindId(rt10_bindId);
        confirmPayResponseVo.setRt11_bankId(rt11_bankId);
        confirmPayResponseVo.setRt12_onlineCardType(rt12_onlineCardType);
        confirmPayResponseVo.setRt13_cardAfterFour(rt13_cardAfterFour);
        confirmPayResponseVo.setRt14_userId(rt14_userId);
        confirmPayResponseVo.setSign(sign);

        posService.payCallback(confirmPayResponseVo);
        HttpClientUtils.writeBackSuccessToResponse(response);
    }

    @RequestMapping(value = "getSign", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 获取微信分享的签名", notes = "获取微信分享的签名")
    public ApiResult<GetSignDto> getSign(
            @RequestBody GetSignRequestDto getSignRequestDto) {
        return posService.getSign(getSignRequestDto.getUrl());
    }

}
