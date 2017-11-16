/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.pos.constants.TransactionStatusType;
import com.pos.pos.constants.UserAuditStatus;
import com.pos.pos.dto.CreateOrderDto;
import com.pos.pos.dto.GetSignDto;
import com.pos.pos.dto.PosUserAuditInfoDto;
import com.pos.pos.dto.auth.AuthorityDetailDto;
import com.pos.pos.dto.auth.AuthorityDto;
import com.pos.pos.dto.get.QuickGetMoneyDto;
import com.pos.pos.dto.identity.IdentifyInfoDto;
import com.pos.pos.dto.request.BindCardDto;
import com.pos.pos.dto.request.GetMoneyDto;
import com.pos.pos.dto.transaction.SelectCardRequestDto;
import com.pos.pos.dto.transaction.TransactionHandledInfoDto;
import com.pos.pos.dto.user.PosUserIdentityDto;
import com.pos.pos.fsm.context.AuditStatusTransferContext;
import com.pos.pos.fsm.context.TransactionStatusTransferContext;
import com.pos.pos.helipay.vo.ConfirmPayResponseVo;

import java.util.Map;

/**
 * 主要业务Service
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
public interface PosService {

    /**
     * 获取快捷收款用户详细信息
     *
     * @param userId 用户userId
     * @return 快捷收款用户详细信息
     */
    AuthorityDetailDto findAuthDetail(Long userId);

    /**
     * 获取快捷收款用户信息
     *
     * @param userId 用户id
     * @return 快捷收款用户信息
     */
    AuthorityDto findAuth(Long userId);

    /**
     * 获取用户身份认证信息-1
     *
     * @param userId    用户id
     * @param decrypted 是否需要解密（true：需要解密返回，false：不需要解密返回）
     * @return 用户身份认证信息，从未提交过身份认证信息则返回空
     */
    PosUserIdentityDto getIdentityInfo(Long userId, boolean decrypted);

    /**
     * 更新用户身份认证信息-1
     *
     * @param userId       用户id
     * @param identityInfo 用户身份认证信息
     * @return 操作结果
     */
    ApiResult<NullObject> updateIdentityInfo(Long userId, PosUserIdentityDto identityInfo);

    /**
     * 获取用户绑定的收款银行卡信息
     *
     * @param userId    用户userId
     * @param decrypted 是否需要解密（true：需要解密返回，false：不需要解密返回）
     * @return 用户绑卡信息，，从未绑定过收款银行卡则返回空
     */
    BindCardDto getBindCardInfo(Long userId, boolean decrypted);

    /**
     * 绑定收款银行卡
     *
     * @param bindCardInfo 绑定的银行卡信息
     * @param userId       用户userId
     * @return 操作结果
     */
    ApiResult<NullObject> bindCard(BindCardDto bindCardInfo, Long userId);

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
     * 用户确认支付并提现
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
     * @param transactionId 交易记录id
     * @param handledInfo   处理信息
     * @param operator      操作人
     * @return 处理结果
     */
    ApiResult<NullObject> handledTransaction(Long transactionId, TransactionHandledInfoDto handledInfo, UserIdentifier operator);

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
