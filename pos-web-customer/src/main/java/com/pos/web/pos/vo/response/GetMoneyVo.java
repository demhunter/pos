/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.response;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 付款之后 跳转到首页 返回给客户端的数据
 * @author 睿智
 * @version 1.0, 2017/8/28
 */
public class GetMoneyVo implements Serializable {

    private static final long serialVersionUID = 7996789976247022381L;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String headImage;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

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
