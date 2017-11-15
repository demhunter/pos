/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.request;

import com.pos.pos.constants.PosConstants;
import com.pos.pos.dto.card.PosCardValidInfoDto;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.service.SecurityService;
import com.pos.common.util.basic.Copyable;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 睿智
 * @version 1.0, 2017/8/23
 */
public class GetMoneyDto implements Serializable, Copyable {

    @ApiModelProperty("金额（BigDecimal, 下单金额）")
    private BigDecimal amount;

    @ApiModelProperty("信用卡卡号")
    private String cardNO;

    @ApiModelProperty("客户姓名-持卡人姓名")
    private String name;

    @ApiModelProperty("身份证-持卡人身份证号")
    private String idCardNO;

    @ApiModelProperty("手机-银行预留手机号")
    private String mobilePhone;

    @ApiModelProperty("* 是否记录银行卡信息，true：记录，false：不记录（有效期和CVV2始终不记录）")
    private boolean recordBankCard;

    @ApiModelProperty("有效期")
    private String validDate;

    @ApiModelProperty("CVV2")
    private String cvv2;

    /**
     * 参数校验
     *
     * @param fieldPrefix     异常提示前缀
     * @param posConstants    参数限制
     * @param securityService 加减密服务
     * @throws IllegalParamException 参数校验异常，参数不合法
     */
    public void check(String fieldPrefix, PosConstants posConstants, SecurityService securityService) throws IllegalParamException {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkMinMaxValue(amount, posConstants.getPosAmountDownLimit(),
                posConstants.getPosAmountUpLimit(), fieldPrefix + "amount");
        FieldChecker.checkEmpty(cardNO, fieldPrefix + "cardNo");
        FieldChecker.checkEmpty(name, fieldPrefix + "name");

        FieldChecker.checkEmpty(this.idCardNO, fieldPrefix + "idCardNO");
        String idCardNo = securityService.decryptData(this.idCardNO);
        FieldChecker.checkEmpty(idCardNo, fieldPrefix + "idCardNO");
        SimpleRegexUtils.checkIdNumber(idCardNo);

        FieldChecker.checkEmpty(this.mobilePhone, fieldPrefix + "mobilePhone");
        String mobilePhone = securityService.decryptData(this.mobilePhone);
        FieldChecker.checkEmpty(mobilePhone, fieldPrefix + "mobilePhone");
        SimpleRegexUtils.isMobile(mobilePhone);
    }

    @Override
    public GetMoneyDto copy() {
        GetMoneyDto result = new GetMoneyDto();
        BeanUtils.copyProperties(this, result);
        return result;
    }

    /**
     * 构建信用卡验证信息Dto<br/>
     * 调用此方法前必须解密数据
     *
     * @return 信用卡验证信息Dto
     */
    public PosCardValidInfoDto buildValidInfo() {
        PosCardValidInfoDto validInfo = new PosCardValidInfoDto();
        validInfo.setCvv2(StringUtils.isEmpty(getCvv2()) ? "" : getCvv2());
        if (StringUtils.isEmpty(getValidDate())) {
            validInfo.setValidYear("");
            validInfo.setValidMonth("");
        } else {
            validInfo.setValidYear(StringUtils.substring(getValidDate(), 2));
            validInfo.setValidMonth(StringUtils.substring(getValidDate(), 0, 2));
        }

        return validInfo;
    }

    public boolean isRecordBankCard() {
        return recordBankCard;
    }

    public void setRecordBankCard(boolean recordBankCard) {
        this.recordBankCard = recordBankCard;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getIdCardNO() {
        return idCardNO;
    }

    public void setIdCardNO(String idCardNO) {
        this.idCardNO = idCardNO;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

}