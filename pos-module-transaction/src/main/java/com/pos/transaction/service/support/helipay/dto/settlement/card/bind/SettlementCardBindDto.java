/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto.settlement.card.bind;

/**
 * 结算卡绑定请求DTO
 *
 * @author wangbing
 * @version 1.0, 2017/12/19
 */
public class SettlementCardBindDto {

    private String P1_bizType; // 交易类型，固定值为：SettlementCardBind

    private String P2_customerNumber; // 商户编号

    private String P3_userId; // 用户id

    private String P4_orderId; // 商户订单号，即record_num

    private String P5_payerName; // 姓名

    private String P6_idCardType; // 证件类型，固定值为：IDCARD，即身份证

    private String P7_idCardNo; // 证件号码，即身份证号码

    private String P8_cardNo; // 银行卡号

    private String P9_phone; // 手机号码，即银行预留手机号

    private String P10_bankUnionCode; // 银行行号，即联行号

    private String P11_operateType; // 操作类型【ADD:绑定；UPDATE:修改；默认ADD】

    public String getP1_bizType() {
        return P1_bizType;
    }

    public void setP1_bizType(String p1_bizType) {
        P1_bizType = p1_bizType;
    }

    public String getP2_customerNumber() {
        return P2_customerNumber;
    }

    public void setP2_customerNumber(String p2_customerNumber) {
        P2_customerNumber = p2_customerNumber;
    }

    public String getP3_userId() {
        return P3_userId;
    }

    public void setP3_userId(String p3_userId) {
        P3_userId = p3_userId;
    }

    public String getP4_orderId() {
        return P4_orderId;
    }

    public void setP4_orderId(String p4_orderId) {
        P4_orderId = p4_orderId;
    }

    public String getP5_payerName() {
        return P5_payerName;
    }

    public void setP5_payerName(String p5_payerName) {
        P5_payerName = p5_payerName;
    }

    public String getP6_idCardType() {
        return P6_idCardType;
    }

    public void setP6_idCardType(String p6_idCardType) {
        P6_idCardType = p6_idCardType;
    }

    public String getP7_idCardNo() {
        return P7_idCardNo;
    }

    public void setP7_idCardNo(String p7_idCardNo) {
        P7_idCardNo = p7_idCardNo;
    }

    public String getP8_cardNo() {
        return P8_cardNo;
    }

    public void setP8_cardNo(String p8_cardNo) {
        P8_cardNo = p8_cardNo;
    }

    public String getP9_phone() {
        return P9_phone;
    }

    public void setP9_phone(String p9_phone) {
        P9_phone = p9_phone;
    }

    public String getP10_bankUnionCode() {
        return P10_bankUnionCode;
    }

    public void setP10_bankUnionCode(String p10_bankUnionCode) {
        P10_bankUnionCode = p10_bankUnionCode;
    }

    public String getP11_operateType() {
        return P11_operateType;
    }

    public void setP11_operateType(String p11_operateType) {
        P11_operateType = p11_operateType;
    }
}
