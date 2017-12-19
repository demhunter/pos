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

    @Value("${pos.register.msg.template}")
    private String posRegisterMsgTemplate; // 用户注册成功通知短信

    @Value("${pos.register.msg.to.parent.template}")
    private String posRegisterMsgToParentTemplate; // 用户注册成功给上级用户的通知短信

    @Value("${pos.audit.submit-all.msg.template}")
    private String posAuditSubmitAllMsgTemplate; // 用户提交实名认证资料通知短信

    @Value("${pos.audit.rejected.msg.template}")
    private String posAuditRejectedMsgTemplate; // 实名认证审核不通过通知短信

    @Value("${pos.level.upgrade.msg.template}")
    private String posLevelUpgradeMsgTemplate; // 等级晋升通知短信

    @Value("${pos.withdraw.rate.msg.template}")
    private String posWithdrawRateMsgTemplate; // 费率变更通知短信

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

    public String getPosRegisterMsgTemplate() {
        return posRegisterMsgTemplate;
    }

    public String getPosRegisterMsgToParentTemplate() {
        return posRegisterMsgToParentTemplate;
    }

    public String getPosAuditSubmitAllMsgTemplate() {
        return posAuditSubmitAllMsgTemplate;
    }

    public String getPosAuditRejectedMsgTemplate() {
        return posAuditRejectedMsgTemplate;
    }

    public String getPosLevelUpgradeMsgTemplate() {
        return posLevelUpgradeMsgTemplate;
    }

    public String getPosWithdrawRateMsgTemplate() {
        return posWithdrawRateMsgTemplate;
    }
}
