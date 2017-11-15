/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.vo.response;

import com.pos.common.util.codec.Base64Utils;
import com.pos.common.util.security.RSAUtils;
import com.pos.pos.constants.CardTypeEnum;
import com.pos.pos.domain.PosBankCard;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class CardBaseInfoVo implements Serializable {

    String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIEbdQU8DGH5rQ0u/o+W+Ojlg8N2eVsQPx0jgY/bUN0Uq9dUVw9vSVJGuG3leuDtyTg8n9+i99KLuTwxio9jnaD22c/OQC2FU6z31PDoNV7ba9XaFyzt1e+2T5PErLgioyNkDvuGIfzAxhwm6v6jhHvvSatU/h8/zzHncRDg9prJAgMBAAECgYAK2sod5IyN+DXqc9cHL4RB4HoRhvZxB46m3oNYGvJThBdmhTrEm3CosDV1V+Pa4WMXjVFLtiHr2795JhkmMRPhgwjJOO7w35SrzsxY1wKjoYsFCUfqLOB2SAVOg4JSMhW+tdvBWDKn4zlgDqmy8a6Uy759ZcuXhYA1psQYsIF5AQJBAOsv65P8C0ARqUs0OTxlK3wsQy58XqHDLiMRxrradv6QzWMwu0XVL7PkmytZY7xVouNx+gqraazgd+xOggooIdECQQCMiFWyx0F31B23ldzG4LYDCh2q6Pp9wcRu7ZWedSkOJRG5Y7lUpl+vzqyPspxTXcBQW/XaffMhZPZtiJyQRW95AkAYJAATXZCuD+IHtSGW4G+ZPFXdBKkWA5nNwbpbXadPM//RCaR/Y4WU+ocu6OsC3utsWzumMrgTJatJlzlj34CxAkAVvt7r7BNAVI1IpCLmj0z6yWzvzl88aGhZ9d+KBn0U2D2W30yFQb1aufNPxQaVi9M/XAt+BLFDgJj1OAdp96SZAkBhYn5HlnYKr1moGkMJ9DBGNW0lpPEke0GhwHfpWOQO1/WySjvRLaVPsyfGtHsZcg1fVYUwUd84tOmlYKLe1YnQ";

    @ApiModelProperty("卡的ID")
    private long id;

    @ApiModelProperty("银行名")
    private String bankName;

    @ApiModelProperty("卡类型 1==借记卡 2==信用卡")
    private byte cardType;

    private String cardTypeDesc;

    @ApiModelProperty("卡号")
    private String cardNO;

    @ApiModelProperty("银行logo的url")
    private String logo;

    public CardBaseInfoVo(){}

    public CardBaseInfoVo(PosBankCard posBankCard, String logo){
        this.id = posBankCard.getId();
        this.bankName = posBankCard.getBankName();
        this.cardType = posBankCard.getCardType();
        this.cardTypeDesc = CardTypeEnum.getEnum(posBankCard.getCardType())!=null ? CardTypeEnum.getEnum(posBankCard.getCardType()).getDesc():"";
//        System.out.println("CardBaseInfoVo.cardNO==============="+posBankCard.getCardNO());
        if(StringUtils.isNotBlank(posBankCard.getBankCardNo())) {
            try {
                this.cardNO = new String(RSAUtils.decryptByPrivateKey(Base64Utils.decryptBASE64(posBankCard.getBankCardNo()), privateKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

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

    public byte getCardType() {
        return cardType;
    }

    public void setCardType(byte cardType) {
        this.cardType = cardType;
    }

    public String getCardTypeDesc() {
        return cardTypeDesc;
    }

    public void setCardTypeDesc(String cardTypeDesc) {
        this.cardTypeDesc = cardTypeDesc;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }
}
