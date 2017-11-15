/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * POS 银行卡信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
public class PosBankCard implements Serializable {

    private static final long serialVersionUID = 1194617060204824186L;

    private Long id;

    private Long userId;

    private String bankCardNo; // 银行卡号

    private String holderName; // 客户姓名

    private String idCardNo; // 身份证号

    private String mobilePhone; // 手机号

    private String bankName; // 银行名字

    private String bankCode; // 银行的代码

    private Byte cardType; // 卡类型

    private Byte cardUsage; // 卡的用途

    private Date createTime;

    private Date lastUseTime;

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

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Date lastUseTime) {
        this.lastUseTime = lastUseTime;
    }
}
