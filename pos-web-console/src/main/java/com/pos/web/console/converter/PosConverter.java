/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.converter;

import com.pos.web.console.vo.pos.CustomerSimpleInfoVo;
import com.pos.pos.dto.user.PosUserIntegrateDto;

import java.math.BigDecimal;

/**
 * 快捷收款用户Converter
 *
 * @author wangbing
 * @version 1.0, 2017/8/25
 */
public class PosConverter {

    public static CustomerSimpleInfoVo toPosUserSimpleInfoVo(PosUserIntegrateDto dto) {
        CustomerSimpleInfoVo vo = new CustomerSimpleInfoVo();

        vo.setId(dto.getId());
        vo.setUserId(dto.getUserId());
        vo.setName(dto.getPosUserInfo().getRealName());
        vo.setPhone(dto.getPosUserInfo().getPhone());
        vo.setRegisterTime(dto.getCreateDate());
        vo.setUserAuditStatus(dto.getAuditStatus());
        vo.setBindingCard(dto.getCardId() != null);
        if (vo.getBindingCard()) {
            vo.setBankName(dto.getBankName());
            vo.setCardNo(dto.getBankCardNO());
        }
        vo.setBaseAuth(dto.buildBaseAuthDto());
        vo.setUserPosCount(dto.getUserPosCount());
        vo.setUserPosAmount(dto.getUserPosAmount());
        if (dto.getCurrentWithdrawDeposit() != null && dto.getCurrentWithdrawDeposit().compareTo(new BigDecimal(0)) > 0) {
            vo.setWithdrawDepositApply(Boolean.TRUE);
            vo.setWithdrawDepositAmount(dto.getCurrentWithdrawDeposit());
        } else {
            vo.setWithdrawDepositApply(Boolean.FALSE);
        }
        vo.setTotalWithdrawDepositAmount(dto.getTotalWithdrawDeposit());

        return vo;
    }
}
