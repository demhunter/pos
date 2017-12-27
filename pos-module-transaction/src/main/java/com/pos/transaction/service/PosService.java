/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service;

import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.transaction.constants.TransactionStatusType;
import com.pos.transaction.dto.CreateOrderDto;
import com.pos.transaction.dto.GetSignDto;
import com.pos.transaction.dto.get.QuickGetMoneyDto;
import com.pos.transaction.dto.request.GetMoneyDto;
import com.pos.transaction.dto.request.LevelUpgradeDto;
import com.pos.transaction.dto.transaction.SelectCardRequestDto;
import com.pos.transaction.dto.transaction.TransactionHandledInfoDto;
import com.pos.transaction.fsm.context.TransactionStatusTransferContext;
import com.pos.transaction.helipay.vo.ConfirmPayResponseVo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public interface PosService {

    /**
     * 获取用户的快捷收款信息
     *
     * @param userId 用户userId
     * @return 快捷收款信息
     */
    ApiResult<QuickGetMoneyDto> getQuickInfo(Long userId);

    /**
     * 用户选卡下单
     *
     * @param userId               用户userId
     * @param selectCardRequestDto 订单信息
     * @param ip                   用户IP地址
     * @return 下单结果
     */
    ApiResult<CreateOrderDto> selectCreateRecord(Long userId, SelectCardRequestDto selectCardRequestDto, String ip);

    /**
     * 填写新信用卡信息，并下单
     *
     * @param getMoneyDto 新信用卡信息和下单信息
     * @param userId      下单用户userId
     * @param ip          下单用户ip地址
     * @return 下单结果
     */
    ApiResult<CreateOrderDto> writeCreateRecord(GetMoneyDto getMoneyDto, Long userId, String ip);

    /**
     * 发送提现确认短信验证码
     *
     * @param userId   用户userId
     * @param recordId 交易id
     * @return 发送结果
     */
    ApiResult<NullObject> sendPayValidateSmsCode(Long userId, Long recordId);

    /**
     * 用户确认提现<br>
     * 到公司账户，提现到用户需要掉另外的接口
     *
     * @param userId   用户userId
     * @param smsCode  短信验证码
     * @param recordId 交易记录id
     * @param ip       用户IP地址
     * @return 提现结果
     */
    ApiResult<NullObject> confirmPay(Long userId, String smsCode, Long recordId, String ip);

    /**
     * 客户确认支付升级
     *
     * @param userId   用户userId
     * @param smsCode  短信验证码
     * @param recordId 交易记录id
     * @param ip       用户IP地址
     * @return 支付升级结果
     */
    ApiResult<NullObject> confirmUpgradeLevel(Long userId, String smsCode, Long recordId, String ip);

    /**
     * 支付回调
     *
     * @param confirmPayResponseVo 合力保回调请求body
     */
    void payCallback(ConfirmPayResponseVo confirmPayResponseVo);

    /**
     * 更新交易记录状态
     *
     * @param context      状态上下文
     * @param targetStatus 目的状态
     */
    boolean updateTransactionStatus(TransactionStatusTransferContext context, TransactionStatusType targetStatus);

    /**
     * 生成基于交易的佣金<br>
     * PS：不考虑交易状态，在调用之前自行确保交易已达到成功终态（交易成功或手动处理成功）
     *
     * @param recordId 交易id
     * @return 生成结果
     */
    boolean generateBrokerage(Long recordId);

    /**
     * 获取银行LOGO相关信息
     *
     * @return 银行LOGO相关信息
     */
    Map<String, String> getAllBankLogo();

    /**
     * 获取微信分享签名
     *
     * @param url 签名地址
     * @return 微信分享签名
     */
    ApiResult<GetSignDto> getSign(String url);

    /**
     * 线下手动处理失败的交易
     *
     * @param recordId    交易记录id
     * @param handledInfo 处理信息
     * @param operator    操作人
     * @return 处理结果
     */
    ApiResult<NullObject> handledTransaction(Long recordId, TransactionHandledInfoDto handledInfo, UserIdentifier operator);

    /**
     * 获取交易的手动处理内容
     *
     * @param recordId 交易id
     * @return 查询结果
     */
    ApiResult<TransactionHandledInfoDto> getHandledTransactionInfo(Long recordId);

    /**
     * 为失败(未成功到账)的交易重发提现请求
     *
     * @param recordId 交易id
     * @param operator 操作人
     * @return 操作结果
     */
    ApiResult<NullObject> againPayToPosUserForFailed(Long recordId, UserIdentifier operator);

    /**
     * 发起佣金提现交易
     *
     * @param permission 佣金提现发起者信息
     * @param brokerage  佣金提现金额
     * @return 提现结果
     */
    ApiResult<BigDecimal> withdrawBrokerage(CustomerPermissionDto permission, BigDecimal brokerage);

    /**
     * 创建一个等级晋升支付交易
     *
     * @param userId           用户id
     * @param levelUpgradeInfo 等级晋升支付信息
     * @param ip               用户IP地址
     * @return 交易信息
     */
    ApiResult<CreateOrderDto> createLevelUpgradeTransaction(Long userId, LevelUpgradeDto levelUpgradeInfo, String ip);
}
