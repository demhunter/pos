/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.transaction.dto.card.PosCardDto;
import com.pos.transaction.dto.request.BindCardDto;

import java.util.List;
import java.util.Map;

/**
 * POS 银行卡相关Service
 *
 * @author wangbing
 * @version 1.0, 2017/10/18
 */
public interface PosCardService {

    /**
     * 获取用户绑定的收款银行卡
     *
     * @param userId    用户id
     * @param decrypted true：需要解密返回，false：不需要解密返回
     * @return 绑定的收款银行卡信息
     */
    BindCardDto getWithdrawCard(Long userId, boolean decrypted);

    /**
     * 获取用户绑定的收款银行卡，返回收款银行卡具体信息
     *
     * @param userId 用户id
     * @return 收款银行卡具体信息
     */
    ApiResult<PosCardDto> findWithdrawCard(Long userId);

    /**
     * 查询用户是否可以更换结算银行卡
     *
     * @param userId 用户id
     * @return 是否可以更换
     */
    ApiResult<NullObject> querySettlementCanAlter(Long userId);

    /**
     * 绑定收款银行卡
     *
     * @param userId       用户userId
     * @param withdrawCard 绑定的银行卡信息
     * @return 操作结果
     */
    ApiResult<NullObject> bindWithdrawCard(Long userId, BindCardDto withdrawCard);

    /**
     * 更换收款银行卡
     *
     * @param userId       用户id
     * @param withdrawCard 新收款银行卡信息
     * @return 更换结果
     */
    ApiResult<NullObject> alterWithdrawCard(Long userId, BindCardDto withdrawCard);

    /**
     * 查询指定用户保存的支付银行卡列表
     *
     * @param userId 用户id
     * @return 支付银行卡列表
     */
    ApiResult<List<PosCardDto>> queryOutBankCard(Long userId);

    /**
     * 删除用户转出银行卡
     *
     * @param cardId 银行卡id
     * @param userId 银行卡所属用户userId
     * @return 操作结果（卡信息不存在，卡用户信息不等，不是转出卡都会返回失败：银行卡信息不存在）
     */
    ApiResult<NullObject> deleteOutBankCard(Long cardId, Long userId);

    /**
     * 查询指定的银行卡信息
     *
     * @param cardIds   银行卡id列表
     * @param decrypted 是否解密数据
     * @return 银行卡信息
     */
    Map<Long, PosCardDto> queryBankCards(List<Long> cardIds, boolean decrypted);

    /**
     * 解密银行卡信息<br/>
     * 此解密返回一个新的对象，需要调用者接收并保存，原对象不变
     *
     * @param source 需要被解密的银行卡
     * @return 解密后的银行卡信息
     */
    void decryptPosCardInfo(PosCardDto source);
}
