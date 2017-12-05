/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.transaction.domain.UserPosCard;
import com.pos.transaction.dto.card.PosCardDto;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class QuickGetMoneyVo implements Serializable {

    private static final long serialVersionUID = -4950421890736780483L;

    // @ApiModelProperty("是否绑定了收款的卡")
    @JsonIgnore
    @Deprecated
    private boolean bindCard;

    @ApiModelProperty("曾经使用的卡信息")
    private List<PosCardDto> cardBaseInfos;

    @ApiModelProperty("手续费率（BigDecimal，具体数值如0.0058）")
    private BigDecimal poundageRate;

    @ApiModelProperty("手续费（BigDecimal，单位：元）")
    private BigDecimal poundage;

    @ApiModelProperty("到账时间")
    private String arrival;

    // @ApiModelProperty("收款银行卡")
    @JsonIgnore
    @Deprecated
    private CardBaseInfoVo getMoneyCard;

    // @ApiModelProperty("昵称")
    @JsonIgnore
    @Deprecated
    private String nickName;

    // @ApiModelProperty("头像")
    @JsonIgnore
    @Deprecated
    private String headImage;

    // @ApiModelProperty("卡号")
    @JsonIgnore
    @Deprecated
    private String cardNO;

    // @ApiModelProperty("银行名")
    @JsonIgnore
    @Deprecated
    private String bankName;

    // @ApiModelProperty("是否显示收款")
    @JsonIgnore
    @Deprecated
    private boolean showGet;

    // @ApiModelProperty("是否显示推广")
    @JsonIgnore
    @Deprecated
    private boolean showSpread;

    // @ApiModelProperty("是否显示发展渠道商")
    @JsonIgnore
    @Deprecated
    private boolean showDevelop;

    public QuickGetMoneyVo(){}

    public QuickGetMoneyVo(List<UserPosCard> inCards, List<UserPosCard> outCards, String poundageRate, String poundage, String arrival, Map<String,String> logos, GetMoneyVo getMoneyVo){
        BeanUtils.copyProperties(getMoneyVo,this);
        if(CollectionUtils.isEmpty(inCards)){
            this.bindCard = false;
        }else{
            this.bindCard = true;
            this.getMoneyCard = new CardBaseInfoVo(inCards.get(0),logos.get(inCards.get(0).getBankCode()));
        }
        this.poundageRate = new BigDecimal(poundageRate);
        this.poundage = new BigDecimal(poundage);
        this.arrival = arrival;
        this.cardBaseInfos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(outCards)){
            //outCards.forEach(e->this.cardBaseInfos.add(new CardBaseInfoVo(e,logos.get(e.getBankCode()))));
        }
    }

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

    public CardBaseInfoVo getGetMoneyCard() {
        return getMoneyCard;
    }

    public void setGetMoneyCard(CardBaseInfoVo getMoneyCard) {
        this.getMoneyCard = getMoneyCard;
    }

    public boolean isBindCard() {
        return bindCard;
    }

    public void setBindCard(boolean bindCard) {
        this.bindCard = bindCard;
    }

    public BigDecimal getPoundageRate() {
        return poundageRate;
    }

    public void setPoundageRate(BigDecimal poundageRate) {
        this.poundageRate = poundageRate;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }
}
