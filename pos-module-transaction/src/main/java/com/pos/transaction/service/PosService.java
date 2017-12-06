/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.transaction.constants.TransactionStatusType;
import com.pos.transaction.constants.UserAuditStatus;
import com.pos.transaction.dto.CreateOrderDto;
import com.pos.transaction.dto.GetSignDto;
import com.pos.transaction.dto.PosUserAuditInfoDto;
import com.pos.transaction.dto.auth.PosUserAuthDetailDto;
import com.pos.transaction.dto.auth.PosUserAuthDto;
import com.pos.transaction.dto.get.QuickGetMoneyDto;
import com.pos.transaction.dto.identity.IdentifyInfoDto;
import com.pos.transaction.dto.request.BindCardDto;
import com.pos.transaction.dto.request.GetMoneyDto;
import com.pos.transaction.dto.transaction.SelectCardRequestDto;
import com.pos.transaction.dto.user.PosUserIdentityDto;
import com.pos.transaction.fsm.context.AuditStatusTransferContext;
import com.pos.transaction.fsm.context.TransactionStatusTransferContext;
import com.pos.transaction.helipay.vo.ConfirmPayResponseVo;
import com.pos.transaction.dto.transaction.TransactionHandledInfoDto;
import com.pos.user.dto.customer.CustomerDto;

import java.util.Map;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public interface PosService {

    /**
     * 更新用户身份认证审核状态
     *
     * @param transferContext 状态转换上下文，包含操作信息
     * @param auditStatus     新身份认证审核状态
     */
    boolean updateAuditStatus(AuditStatusTransferContext transferContext, UserAuditStatus auditStatus);

    /**
     * 获取指定用户的身份认证审核信息
     *
     * @param posAuthId user_pos_auth主键id
     * @param decrypted 是否需要解密（true：需要解密返回，false：不需要解密返回）
     * @return 用户的身份认证审核信息
     */
    ApiResult<PosUserAuditInfoDto> getAuditInfo(Long posAuthId, boolean decrypted);

    /**
     * 管理员审核用户身份认证信息
     *
     * @param identifyInfo 审核操作信息
     * @return 审核操作结果
     */
    ApiResult<NullObject> identifyPosUserInfo(IdentifyInfoDto identifyInfo);

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
}
