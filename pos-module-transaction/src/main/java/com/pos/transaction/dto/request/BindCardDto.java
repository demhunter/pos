/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.request;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 绑卡信息Dto
 *
 * @author 睿智
 * @version 1.0, 2017/8/23
 */
public class BindCardDto implements Serializable {

    private static final long serialVersionUID = 828115046411196456L;

    @ApiModelProperty("绑定的银行卡id（第一次绑定收款卡时不传此字段表示新增，做更新操作时需要回传此字段）")
    private Long posCardId;

    @ApiModelProperty("持卡人姓名")
    private String name;

    @ApiModelProperty("银行储蓄卡卡号")
    private String cardNO;

    @ApiModelProperty("预留手机号")
    private String phone;

    @ApiModelProperty("对应收款银行卡的正面照")
    private String posCardImage;

    @ApiModelProperty("当身份认证未通过时，绑卡照片是否允许修改，false = 不允许修改（PS：当重新提交认证时，只能更改照片信息，其它字段均不允许更新）")
    private Boolean posCardImageCanModify;

    public Long getPosCardId() {
        return posCardId;
    }

    public void setPosCardId(Long posCardId) {
        this.posCardId = posCardId;
    }

    public String getPosCardImage() {
        return posCardImage;
    }

    public void setPosCardImage(String posCardImage) {
        this.posCardImage = posCardImage;
    }

    public Boolean getPosCardImageCanModify() {
        return posCardImageCanModify;
    }

    public void setPosCardImageCanModify(Boolean posCardImageCanModify) {
        this.posCardImageCanModify = posCardImageCanModify;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
