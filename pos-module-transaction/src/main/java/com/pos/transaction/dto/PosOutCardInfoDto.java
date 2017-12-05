/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 转出卡信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class PosOutCardInfoDto implements Serializable {

    private static final long serialVersionUID = -5260981225859330818L;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("银行代码")
    private String bankCode;

    @ApiModelProperty("银行卡号后四位")
    private String cardNo;

    @ApiModelProperty("银行卡类型（int, 1==借记卡 2==信用卡）")
    private Byte cardType;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Byte getCardType() {
        return cardType;
    }

    public void setCardType(Byte cardType) {
        this.cardType = cardType;
    }
}
