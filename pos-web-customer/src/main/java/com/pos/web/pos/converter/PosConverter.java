/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.converter;

import com.pos.transaction.constants.CardTypeEnum;
import com.pos.transaction.dto.transaction.TransactionRecordDto;
import com.pos.web.pos.vo.brokerage.BrokerageAppliedRecordVo;
import com.pos.web.pos.vo.response.RecordVo;

import java.util.Map;

/**
 * PosConverter
 *
 * @author wangbing
 * @version 1.0, 2017/10/20
 */
public class PosConverter {

    public static RecordVo toRecordVo(TransactionRecordDto dto, Map<String, String> logoMap) {
        RecordVo vo = new RecordVo();

        vo.setId(dto.getId());
        vo.setTransactionDate(dto.getPayDate());
        vo.setAmount(dto.getAmount());
        vo.setServiceCharge(dto.getServiceCharge());
        vo.setArrivalAmount(dto.getArrivalAmount());
        vo.setArrived(dto.parseTransactionStatus().isArrived());
        if (dto.getOutCardInfo() != null) {
            vo.setCardNO(dto.getOutCardInfo().getCardNo());
            vo.setBankName(dto.getOutCardInfo().getBankName());
            CardTypeEnum cardType = CardTypeEnum.getEnum(dto.getOutCardInfo().getCardType());
            vo.setCardType(cardType == null ? null : cardType.getDesc());
            vo.setBankLogo(logoMap.get(dto.getOutCardInfo().getBankCode()));
        }

        return vo;
    }

    public static BrokerageAppliedRecordVo toBrokerageAppliedRecordVo(TransactionRecordDto dto) {
        BrokerageAppliedRecordVo vo = new BrokerageAppliedRecordVo();

        vo.setId(dto.getId());
        vo.setAmount(dto.getAmount());
        vo.setStatus(dto.getStatus());
        vo.setStatusDesc(dto.parseTransactionStatus().getDesc());
        vo.setCreateTime(dto.getCreateDate());

        return vo;
    }
}
