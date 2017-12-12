/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.request;

import com.pos.basic.service.SecurityService;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 付款晋升等级信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/7
 */
public class LevelUpgradeDto implements Serializable {

    private static final long serialVersionUID = 6314941923284682072L;
    @ApiModelProperty("晋升目标等级")
    private Integer targetLevel;

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

    /**
     * 参数校验
     *
     * @param fieldPrefix     异常提示前缀
     * @param securityService 加减密服务
     * @throws IllegalParamException 参数校验异常，参数不合法
     */
    public void check(String fieldPrefix, SecurityService securityService) throws IllegalParamException {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(targetLevel, fieldPrefix + "targetLevel");
        FieldChecker.checkEmpty(amount, fieldPrefix + "amount");
        FieldChecker.checkEmpty(bankCardNo, fieldPrefix + "bankCardNo");

        FieldChecker.checkEmpty(this.mobilePhone, fieldPrefix + "mobilePhone");
        String mobilePhone = securityService.decryptData(this.mobilePhone);
        FieldChecker.checkEmpty(mobilePhone, fieldPrefix + "mobilePhone");
        Validator.checkMobileNumber(mobilePhone);
    }

    public Integer getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(Integer targetLevel) {
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
