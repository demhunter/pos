/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.auth;

import com.pos.transaction.constants.CardTypeEnum;
import com.pos.transaction.constants.UserAuditStatus;
import com.pos.transaction.dto.card.PosCardDto;
import com.pos.transaction.dto.request.BindCardDto;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.service.SecurityService;

import java.io.Serializable;
import java.util.Date;

/**
 * 快捷收款用户详细信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/16
 */
public class PosUserAuthDetailDto extends PosUserAuthDto implements Serializable {

    @ApiModelProperty("银行卡号")
    private String bankCardNO;

    @ApiModelProperty("客户姓名-持卡人姓名")
    private String bankCardName;

    @ApiModelProperty("身份证号-持卡人身份证号码")
    private String bankCardIdCardNO;

    @ApiModelProperty("手机号-银行预留手机号")
    private String mobilePhone;

    @ApiModelProperty("银行名字-银行卡所属银行名称")
    private String bankName;

    @ApiModelProperty("银行的代码")
    private String bankCode;

    @ApiModelProperty("银行LOGO")
    private String bankLogo;

    @ApiModelProperty("* 银行灰度LOGO")
    private String bankGrayLogo;

    /** @see CardTypeEnum#code */
    @ApiModelProperty("卡类型：1 = 储蓄卡，2 = 信用卡")
    private byte cardType;

    @ApiModelProperty("卡的用途：1 = 转入(收款银行卡)，2 = 转出")
    private byte cardUsage;

    @ApiModelProperty("收款银行卡最近使用时间（Date）")
    private Date lastUseDate;

    public BindCardDto buildBindCardInfo(boolean decrypted, SecurityService securityService) {
        BindCardDto bindCardInfo = new BindCardDto();
        bindCardInfo.setPosCardId(getPosCardId());
        bindCardInfo.setName(getBankCardName());
        bindCardInfo.setCardNO(getBankCardNO());
        bindCardInfo.setPhone(getMobilePhone());
        if (decrypted) {
            bindCardInfo.setName(securityService.decryptData(bindCardInfo.getName()));
            bindCardInfo.setCardNO(securityService.decryptData(bindCardInfo.getCardNO()));
            bindCardInfo.setPhone(securityService.decryptData(bindCardInfo.getPhone()));
        }
        bindCardInfo.setPosCardImage(getPosCardImage());
        // 身份认证已通过和未审核的图片不允许修改，其它均可修改
        if (UserAuditStatus.AUDITED.equals(parseAuditStatus())
                || UserAuditStatus.NOT_AUDIT.equals(parseAuditStatus())) {
            bindCardInfo.setPosCardImageCanModify(Boolean.FALSE);
        } else {
            bindCardInfo.setPosCardImageCanModify(Boolean.TRUE);
        }

        return bindCardInfo;
    }

    /**
     * 构建用户绑定的收款银行卡<br/>
     * 如果用户未绑定收款银行卡(即用户身份认证信息未提交)，则返回null
     *
     * @return 用户绑定的收款银行卡信息
     */
    public PosCardDto buildUserInCard() {
        if (UserAuditStatus.NOT_SUBMIT.equals(parseAuditStatus())) {
            return null;
        }
        PosCardDto inCard = new PosCardDto();
        inCard.setId(getPosCardId());
        inCard.setUserId(getUserId());
        inCard.setName(getBankCardName());
        inCard.setIdCardNo(getBankCardIdCardNO());
        inCard.setCardNO(getBankCardNO());
        inCard.setMobilePhone(getMobilePhone());
        inCard.setCardType(getCardType());
        inCard.setBankName(getBankName());
        inCard.setLogo(getBankLogo());
        inCard.setCardUsage(getCardUsage());
        inCard.setLastUseDate(getLastUseDate());
        return inCard;
    }

    public String getBankGrayLogo() {
        return bankGrayLogo;
    }

    public void setBankGrayLogo(String bankGrayLogo) {
        this.bankGrayLogo = bankGrayLogo;
    }

    public Date getLastUseDate() {
        return lastUseDate;
    }

    public void setLastUseDate(Date lastUseDate) {
        this.lastUseDate = lastUseDate;
    }

    public String getBankCardNO() {
        return bankCardNO;
    }

    public void setBankCardNO(String bankCardNO) {
        this.bankCardNO = bankCardNO;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getBankCardIdCardNO() {
        return bankCardIdCardNO;
    }

    public void setBankCardIdCardNO(String bankCardIdCardNO) {
        this.bankCardIdCardNO = bankCardIdCardNO;
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

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public byte getCardType() {
        return cardType;
    }

    public void setCardType(byte cardType) {
        this.cardType = cardType;
    }

    public byte getCardUsage() {
        return cardUsage;
    }

    public void setCardUsage(byte cardUsage) {
        this.cardUsage = cardUsage;
    }
}
