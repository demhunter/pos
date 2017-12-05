/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class PosLoginDto implements Serializable {

    private static final long serialVersionUID = -260263195365477017L;

    @ApiModelProperty("卡号")
    private String cardNO;

    @ApiModelProperty("银行名")
    private String bankName;

    @ApiModelProperty("是否显示收款")
    private boolean showGet;

    @ApiModelProperty("是否显示推广")
    private boolean showSpread;

    @ApiModelProperty("是否显示发展渠道商")
    private boolean showDevelop;

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isShowGet() {
        return showGet;
    }

    public void setShowGet(boolean showGet) {
        this.showGet = showGet;
    }

    public boolean isShowSpread() {
        return showSpread;
    }

    public void setShowSpread(boolean showSpread) {
        this.showSpread = showSpread;
    }

    public boolean isShowDevelop() {
        return showDevelop;
    }

    public void setShowDevelop(boolean showDevelop) {
        this.showDevelop = showDevelop;
    }
}
