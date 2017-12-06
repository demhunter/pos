/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.Copyable;
import com.pos.transaction.constants.CardTypeEnum;
import com.pos.transaction.constants.CardUsageEnum;
import com.pos.transaction.dto.PosOutCardInfoDto;
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
    private String name;

    @ApiModelProperty("持卡人身份证号")
    private String idCardNo;

    @ApiModelProperty("卡号")
    private String cardNO;

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
    private Date lastUseDate;

    @ApiModelProperty("v2.0.0 * 银行卡是否有效，在为收款银行卡时，此字段有效（Date）")
    private Boolean available;

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
        if (!StringUtils.isEmpty(cardNO)) {
            simpleOut.setCardNo(cardNO.substring(cardNO.length() - 4, cardNO.length()));
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

    public Date getLastUseDate() {
        return lastUseDate;
    }

    public void setLastUseDate(Date lastUseDate) {
        this.lastUseDate = lastUseDate;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
