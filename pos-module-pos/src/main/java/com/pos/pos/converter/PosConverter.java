/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.converter;

import com.pos.common.util.basic.JsonUtils;
import com.pos.pos.domain.UserPosTransactionRecord;
import com.pos.pos.dto.PosOutCardInfoDto;
import com.pos.pos.dto.card.PosCardDto;
import com.pos.pos.dto.request.GetMoneyDto;
import com.pos.pos.constants.CardUsageEnum;
import com.pos.pos.domain.UserPosCard;
import com.pos.pos.dto.transaction.TransactionRecordDto;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.BeanUtils;

/**
 * POS 相关转换类
 *
 * @author wangbing
 * @version 1.0, 2017/10/18
 */
public class PosConverter {

    public static PosCardDto toPosCardDto(UserPosCard card) {
        PosCardDto dto = new PosCardDto();

        BeanUtils.copyProperties(card, dto);
        dto.setBankName(card.getBank());

        return dto;
    }

    public static UserPosCard toUserPosCard(PosCardDto dto) {
        UserPosCard card = new UserPosCard();

        BeanUtils.copyProperties(dto, card);
        card.setBank(dto.getBankName());
        card.setIdCardNO(dto.getIdCardNo());

        return card;
    }

    public static PosCardDto toPosCardDto(GetMoneyDto getMoneyDto, Long userId) {
        PosCardDto dto = new PosCardDto();

        dto.setUserId(userId);
        dto.setName(getMoneyDto.getName());
        dto.setIdCardNo(getMoneyDto.getIdCardNO());
        dto.setCardNO(getMoneyDto.getCardNO());
        dto.setMobilePhone(getMoneyDto.getMobilePhone());
        dto.setCardUsage(CardUsageEnum.OUT_CARD.getCode());

        return dto;
    }

    public static TransactionRecordDto toTransactionRecordDto(UserPosTransactionRecord record) {
        TransactionRecordDto transaction = new TransactionRecordDto();

        BeanUtils.copyProperties(record, transaction);
        if (StringUtils.isNotEmpty(record.getOutCardInfo())) {
            PosOutCardInfoDto outCardInfo = JsonUtils.jsonToObject(record.getOutCardInfo(), new TypeReference<PosOutCardInfoDto>() {});
            transaction.setOutCardInfo(outCardInfo);
        }

        return transaction;
    }
}
