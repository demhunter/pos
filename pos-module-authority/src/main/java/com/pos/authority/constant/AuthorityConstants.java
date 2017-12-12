/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 权限相关配置信息
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
@Component
public class AuthorityConstants {

    @Value("${pos.withdraw.basic.rate}")
    private String posWithdrawBasicRate; // 提现的总手续费基准比例（具体数值，如0.58%为0.0058）

    @Value("${pos.arrival.time}")
    private String posArrivalTime; // 收款提现到账时间 D+0

    @Value("${pos.withdraw.rate.down.limit}")
    private String posWithdrawRateDownLimit; // 提现手续费下限

    @Value("${pos.extra.service.charge.down.limit}")
    private String posExtraServiceChargeDownLimit; // 提现额外手续费下限

    public BigDecimal getPosWithdrawBasicRate() {
        return StringUtils.isEmpty(posWithdrawBasicRate) ? BigDecimal.ZERO : new BigDecimal(posWithdrawBasicRate);
    }

    public String getPosArrivalTime() {
        return posArrivalTime;
    }

    public BigDecimal getPosWithdrawRateDownLimit() {
        return StringUtils.isEmpty(posWithdrawRateDownLimit) ? BigDecimal.ZERO : new BigDecimal(posWithdrawRateDownLimit);
    }

    public BigDecimal getPosExtraServiceChargeDownLimit() {
        return StringUtils.isEmpty(posExtraServiceChargeDownLimit) ? BigDecimal.ZERO : new BigDecimal(posExtraServiceChargeDownLimit);
    }
}
