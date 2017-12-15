/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.converter;

import com.pos.authority.dto.customer.CustomerIntegrateInfoDto;
import com.pos.web.console.vo.pos.PosUserSimpleInfoVo;

import java.math.BigDecimal;

/**
 * 快捷收款用户Converter
 *
 * @author wangbing
 * @version 1.0, 2017/8/25
 */
public class PosConverter {

    public static PosUserSimpleInfoVo toPosUserSimpleInfoVo(CustomerIntegrateInfoDto dto) {
        PosUserSimpleInfoVo vo = new PosUserSimpleInfoVo();

        // 自身账号信息
        vo.setId(dto.getId());
        vo.setUserId(dto.getUserId());
        vo.setName(dto.getName());
        vo.setPhone(dto.getPhone());
        vo.setRegisterTime(dto.getRegisterTime());
        vo.setUserAvailable(dto.getUserAvailable());

        // 上级信息
        vo.setExistedParent(dto.getExistedParent());
        if (vo.getExistedParent()) {
            vo.setParentUserId(dto.getParentUserId());
            vo.setParentName(dto.getParentName());
            vo.setParentPhone(dto.getParentPhone());
        }
        vo.setUserAuditStatus(dto.getUserAuditStatus());

        // 绑卡信息
        vo.setPosCardId(dto.getPosCardId());

        // 权限及等级信息
        vo.setLevel(dto.getLevel());
        vo.setWithdrawRate(dto.getWithdrawRate().multiply(new BigDecimal("100")));
        vo.setExtraServiceCharge(dto.getExtraServiceCharge());

        // 统计信息
        vo.setUserPosCount(dto.getUserPosCount());
        vo.setUserPosAmount(dto.getUserPosAmount());
        vo.setChildrenCount(dto.getChildrenCount());
        vo.setDescendantCount(dto.getDescendantCount());
        vo.setCurrentBrokerage(dto.getCurrentBrokerage());
        vo.setBrokerageAppliedCount(dto.getBrokerageAppliedCount());
        vo.setAppliedBrokerage(dto.getAppliedBrokerage());
        vo.setInterviewCount(dto.getInterviewCount());
        vo.setUserPosCount(dto.getUserPosCount());
        vo.setUserPosAmount(dto.getUserPosAmount());

        return vo;
    }
}
