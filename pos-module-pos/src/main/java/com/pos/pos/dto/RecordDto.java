/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class RecordDto implements Serializable {

    private static final long serialVersionUID = 2186992201468601653L;

    @ApiModelProperty("id")
    private long id;

    @ApiModelProperty("银行名字")
    private String bankName;

    @ApiModelProperty("交易时间")
    private Date transactionDate;

    @ApiModelProperty("提现金额")
    private BigDecimal amount;

    @ApiModelProperty("手续费")
    private BigDecimal serviceCharge;

    @ApiModelProperty("实际到账金额")
    private BigDecimal arrivalAmount;

    @ApiModelProperty("卡号")
    private String cardNO;

    @ApiModelProperty("卡类型")
    private Byte cardType;

    @ApiModelProperty("银行代码")
    private String bankCode;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Byte getCardType() {
        return cardType;
    }

    public void setCardType(Byte cardType) {
        this.cardType = cardType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getArrivalAmount() {
        return arrivalAmount;
    }

    public void setArrivalAmount(BigDecimal arrivalAmount) {
        this.arrivalAmount = arrivalAmount;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }
}
