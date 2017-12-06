/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * POS 银行卡信息
 *
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class UserPosCard implements Serializable {

    private static final long serialVersionUID = 1194617060204824186L;

    private Long id;

    private Long userId;

    private String cardNO; // 银行卡号

    private String name; // 客户姓名

    private String idCardNO; // 身份证号

    private String mobilePhone; // 手机号

    private String bank; // 银行名字

    private String bankCode; // 银行的代码

    private Byte cardType; // 卡类型

    private Byte cardUsage; // 卡的用途

    private Date createDate;

    private Date lastUseDate;

    private Boolean available; // v2.0.0 * 银行卡是否有效，在为收款银行卡时，此字段有效

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getCardType() {
        return cardType;
    }

    public void setCardType(Byte cardType) {
        this.cardType = cardType;
    }

    public Byte getCardUsage() {
        return cardUsage;
    }

    public void setCardUsage(Byte cardUsage) {
        this.cardUsage = cardUsage;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Date getLastUseDate() {
        return lastUseDate;
    }

    public void setLastUseDate(Date lastUseDate) {
        this.lastUseDate = lastUseDate;
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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
