/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.level;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 等级晋升VO
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class LevelUpgradeVo implements Serializable {

    private static final long serialVersionUID = -6553788485016234669L;
    @ApiModelProperty("晋升目标等级")
    private Long targetLevel;

    @ApiModelProperty("晋升支付金额")
    private BigDecimal amount;

    @ApiModelProperty("支付银行卡卡号")
    private String bankCardNo;

    @ApiModelProperty("银行卡预留手机号")
    private String mobilePhone;

    @ApiModelProperty("是否记录银行卡信息，true：记录，false：不记录（有效期和CVV2始终不记录）")
    private boolean recordBankCard;

    @ApiModelProperty("有效期")
    private String validDate;

    @ApiModelProperty("CVV2")
    private String cvv2;

    public Long getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(Long targetLevel) {
        this.targetLevel = targetLevel;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public boolean isRecordBankCard() {
        return recordBankCard;
    }

    public void setRecordBankCard(boolean recordBankCard) {
        this.recordBankCard = recordBankCard;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }
}
