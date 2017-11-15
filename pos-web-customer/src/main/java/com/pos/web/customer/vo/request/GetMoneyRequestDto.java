/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.vo.request;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class GetMoneyRequestDto implements Serializable {

    @ApiModelProperty("金额")
    private String amount;

    @ApiModelProperty("信用卡卡号")
    private String cardNO;

    @ApiModelProperty("客户姓名")
    private String name;

    @ApiModelProperty("身份证")
    private String idCardNO;

    @ApiModelProperty("手机")
    private String mobilePhone;

    @ApiModelProperty("* 是否记录银行卡信息，true：记录，false：不记录（有效期和CVV2始终不记录）")
    private boolean recordBankCard;

    @ApiModelProperty("有效期")
    private String validDate;

    @ApiModelProperty("CVV2")
    private String cvv2;

    public boolean isRecordBankCard() {
        return recordBankCard;
    }

    public void setRecordBankCard(boolean recordBankCard) {
        this.recordBankCard = recordBankCard;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNO() {
        return idCardNO;
    }

    public void setIdCardNO(String idCardNO) {
        this.idCardNO = idCardNO;
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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

}
