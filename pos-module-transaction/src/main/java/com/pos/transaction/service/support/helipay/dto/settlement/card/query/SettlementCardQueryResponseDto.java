/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto.settlement.card.query;

import com.pos.transaction.service.support.helipay.dto.HelibaoBasicResponse;

/**
 * 结算卡查询返回结果Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/21
 */
public class SettlementCardQueryResponseDto extends HelibaoBasicResponse {

    private String rt1_bizType; // 交易类型，固定值为：SettlementCardQuery

    private String rt2_retCode; // 返回码，返回0000即成功，只有成功才会有结算卡信息

    private String rt3_retMsg; // 返回信息

    private String rt4_customerNumber; // 商户编号

    private String rt5_userId; // 用户id

    private String rt6_payerName; // 姓名

    private String rt7_idType; // 证件类型，[IDCARD, 表示身份证件]

    private String rt8_idCardNo; // 证件号码，[身份证号]

    private String rt9_cardNo; // 卡号

    private String rt10_bankCode; // 银行编码

    private String rt11_onlineCardType; // 卡类型

    private String rt12_payerPhone; // 手机号

    private String rt13_createDate; // 创建时间

    private String sign; // 签名

    @Override
    public String getRt1_bizType() {
        return rt1_bizType;
    }

    public void setRt1_bizType(String rt1_bizType) {
        this.rt1_bizType = rt1_bizType;
    }

    @Override
    public String getRt2_retCode() {
        return rt2_retCode;
    }

    public void setRt2_retCode(String rt2_retCode) {
        this.rt2_retCode = rt2_retCode;
    }

    @Override
    public String getRt3_retMsg() {
        return rt3_retMsg;
    }

    public void setRt3_retMsg(String rt3_retMsg) {
        this.rt3_retMsg = rt3_retMsg;
    }

    @Override
    public String getRt4_customerNumber() {
        return rt4_customerNumber;
    }

    public void setRt4_customerNumber(String rt4_customerNumber) {
        this.rt4_customerNumber = rt4_customerNumber;
    }

    public String getRt5_userId() {
        return rt5_userId;
    }

    public void setRt5_userId(String rt5_userId) {
        this.rt5_userId = rt5_userId;
    }

    public String getRt6_payerName() {
        return rt6_payerName;
    }

    public void setRt6_payerName(String rt6_payerName) {
        this.rt6_payerName = rt6_payerName;
    }

    public String getRt7_idType() {
        return rt7_idType;
    }

    public void setRt7_idType(String rt7_idType) {
        this.rt7_idType = rt7_idType;
    }

    public String getRt8_idCardNo() {
        return rt8_idCardNo;
    }

    public void setRt8_idCardNo(String rt8_idCardNo) {
        this.rt8_idCardNo = rt8_idCardNo;
    }

    public String getRt9_cardNo() {
        return rt9_cardNo;
    }

    public void setRt9_cardNo(String rt9_cardNo) {
        this.rt9_cardNo = rt9_cardNo;
    }

    public String getRt10_bankCode() {
        return rt10_bankCode;
    }

    public void setRt10_bankCode(String rt10_bankCode) {
        this.rt10_bankCode = rt10_bankCode;
    }

    public String getRt11_onlineCardType() {
        return rt11_onlineCardType;
    }

    public void setRt11_onlineCardType(String rt11_onlineCardType) {
        this.rt11_onlineCardType = rt11_onlineCardType;
    }

    public String getRt12_payerPhone() {
        return rt12_payerPhone;
    }

    public void setRt12_payerPhone(String rt12_payerPhone) {
        this.rt12_payerPhone = rt12_payerPhone;
    }

    public String getRt13_createDate() {
        return rt13_createDate;
    }

    public void setRt13_createDate(String rt13_createDate) {
        this.rt13_createDate = rt13_createDate;
    }

    @Override
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
