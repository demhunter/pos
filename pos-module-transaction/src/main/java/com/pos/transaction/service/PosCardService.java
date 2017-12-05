/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;

/**
 * POS 银行卡相关Service
 *
 * @author wangbing
 * @version 1.0, 2017/10/18
 */
public interface PosCardService {

    /**
     * 删除用户转出银行卡
     *
     * @param cardId     银行卡id
     * @param cardUserId 银行卡所属用户userId
     * @return 操作结果（卡信息不存在，卡用户信息不等，不是转出卡都会返回失败：银行卡信息不存在）
     */
    ApiResult<NullObject> deleteOutBankCard(Long cardId, Long cardUserId);
}
