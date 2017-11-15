/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.pos.dto.PosOutCardInfoDto;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.Copyable;
import com.pos.pos.constants.CardTypeEnum;
import com.pos.pos.constants.CardUsageEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 银行卡信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/18
 */
public class PosCardDto implements Serializable, Copyable {

    private static final long serialVersionUID = -8394545328853120624L;

    @ApiModelProperty("卡的ID")
    private Long id;

    @ApiModelProperty("银行卡所属用户userId")
    private Long userId;

    @ApiModelProperty("持卡人姓名")
    private String holderName;

    @ApiModelProperty("持卡人身份证号")
    private String idCardNo;

    @ApiModelProperty("卡号")
    private String bankCardNo;

    @ApiModelProperty("银行预留手机号")
    private String mobilePhone;

    @ApiModelProperty("卡类型(int, 1==借记卡 2==信用卡)")
    private Byte cardType;

    @ApiModelProperty("卡类型描述")
    public String getCardTypeDesc() {
        return cardType == null ? null : CardTypeEnum.getEnum(cardType).getDesc();
    }

    @ApiModelProperty("银行名")
    private String bankName;

    @ApiModelProperty("银行code")
    private String bankCode;

    @ApiModelProperty("银行logo")
    private String logo;

    @ApiModelProperty("卡用途(int, 1==转入（收款银行卡） 2==转出)")
    private Byte cardUsage;

    @ApiModelProperty("银行卡最近使用时间（Date）")
    private Date lastUseTime;

    @JsonIgnore
    private PosCardValidInfoDto validInfo; // 下单时填写的CVV2和有效期信息

    @Override
    public PosCardDto copy() {
        PosCardDto card = new PosCardDto();
        BeanUtils.copyProperties(this, card);
        return card;
    }

    public PosOutCardInfoDto buildSimplePosOutCard() {
        PosOutCardInfoDto simpleOut = new PosOutCardInfoDto();
        if (!StringUtils.isEmpty(bankCardNo)) {
            simpleOut.setCardNo(bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length()));
        }
        simpleOut.setBankCode(bankCode);
        simpleOut.setBankName(bankName);
        simpleOut.setCardType(cardType);

        return simpleOut;
    }

    public CardUsageEnum parseCardUsage() {
        return cardUsage == null ? null : CardUsageEnum.getEnum(cardUsage);
    }

    public CardTypeEnum parseCardType() {
        return cardType == null ? null : CardTypeEnum.getEnum(cardType);
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public PosCardValidInfoDto getValidInfo() {
        return validInfo;
    }

    public void setValidInfo(PosCardValidInfoDto validInfo) {
        this.validInfo = validInfo;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

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

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Byte getCardType() {
        return cardType;
    }

    public void setCardType(Byte cardType) {
        this.cardType = cardType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Byte getCardUsage() {
        return cardUsage;
    }

    public void setCardUsage(Byte cardUsage) {
        this.cardUsage = cardUsage;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public Date getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Date lastUseTime) {
        this.lastUseTime = lastUseTime;
    }
}
