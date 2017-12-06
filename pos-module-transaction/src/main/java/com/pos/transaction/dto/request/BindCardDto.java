/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.request;

import com.pos.basic.service.SecurityService;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
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

    /**
     * 更新是检查字段信息是否符合要求
     *
     * @param fieldPrefix     提示前缀
     * @param securityService 解密服务（姓名和身份证号为加密数据，校验之前需要解密）
     * @throws IllegalParamException 参数异常
     */
    public void check(String fieldPrefix, SecurityService securityService) throws IllegalParamException {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";

        FieldChecker.checkEmpty(this.cardNO, fieldPrefix + "cardNO");
        FieldChecker.checkEmpty(this.posCardImage, fieldPrefix + "posCardImage");

        FieldChecker.checkEmpty(this.phone, fieldPrefix + "phone");
        String phone = securityService.decryptData(this.phone);
        Validator.checkMobileNumber(phone);
    }

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
