/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.impl;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.dao.PosCardDao;
import com.pos.pos.exception.PosUserErrorCode;
import com.pos.pos.constants.CardUsageEnum;
import com.pos.pos.dto.card.PosCardDto;
import com.pos.pos.service.PosCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * POS 银行卡相关ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/10/18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosCardServiceImpl implements PosCardService {

    @Resource
    private PosCardDao posCardDao;

    @Override
    public ApiResult<NullObject> deleteOutBankCard(Long cardId, Long cardUserId) {
        FieldChecker.checkEmpty(cardId, "cardId");
        FieldChecker.checkEmpty(cardUserId, "cardUserId");

        PosCardDto posCard = posCardDao.getUserPosCard(cardId);
        if (posCard == null
                || !cardUserId.equals(posCard.getUserId())
                || !CardUsageEnum.OUT_CARD.equals(posCard.parseCardUsage())) {
            return ApiResult.fail(PosUserErrorCode.BANK_CARD_NOT_EXISTED);
        }
        posCardDao.delete(cardId);
        return ApiResult.succ();
    }
}
