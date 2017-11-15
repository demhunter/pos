/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.vo.response;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class RecordVo implements Serializable {

    private static final long serialVersionUID = -2706203715030840810L;

    @ApiModelProperty("id")
    private Long id;

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

    @ApiModelProperty("是否已到账（true：已到账，false：未到账）")
    private Boolean arrived;

    @ApiModelProperty("* 卡号后四位")
    private String cardNO;

    @ApiModelProperty("卡类型")
    private String cardType;

    @ApiModelProperty("银行logo")
    private String bankLogo;

    public Boolean getArrived() {
        return arrived;
    }

    public void setArrived(Boolean arrived) {
        this.arrived = arrived;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
