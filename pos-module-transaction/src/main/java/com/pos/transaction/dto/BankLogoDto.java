/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/8/27
 */
public class BankLogoDto implements Serializable {

    private static final long serialVersionUID = -7009438778731264179L;

    private long id;

    private String bankName;

    private String bankLogo;

    private String bankCode;

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

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
