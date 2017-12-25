/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.helipay.vo;

import java.io.Serializable;

/**
 * 商户提现请求信息
 *
 * @author wangbing
 * @version 1.0, 2017/12/25
 */
public class MerchantWithdrawVo implements Serializable {

    private static final long serialVersionUID = 3262893883524614397L;

    private String P1_bizType; // 交易类型，固定值为：MerchantWithdraw

    private String P2_customerNumber; // 商户编号

    private String P3_orderId; // 商户订单号

    private String P4_amount; // 交易金额

    private String P5_bankCode; // 银行编码

    private String P6_bankAccountNo; // 银行账户号

    private String P7_bankAccountName; // 银行账号名

    private String P8_biz; // 业务类型[B2B:对公、B2C:对私]

    private String P9_bankUnionCode; // 银行联行号[对公联行号必填]

    private String P10_feeType; // 手续费收取方式[PAYER:付款方收取手续费、RECEIVER:收款方收取手续费]

    private String P11_summary; // 提现备注

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

    public String getP3_orderId() {
        return P3_orderId;
    }

    public void setP3_orderId(String p3_orderId) {
        P3_orderId = p3_orderId;
    }

    public String getP4_amount() {
        return P4_amount;
    }

    public void setP4_amount(String p4_amount) {
        P4_amount = p4_amount;
    }

    public String getP5_bankCode() {
        return P5_bankCode;
    }

    public void setP5_bankCode(String p5_bankCode) {
        P5_bankCode = p5_bankCode;
    }

    public String getP6_bankAccountNo() {
        return P6_bankAccountNo;
    }

    public void setP6_bankAccountNo(String p6_bankAccountNo) {
        P6_bankAccountNo = p6_bankAccountNo;
    }

    public String getP7_bankAccountName() {
        return P7_bankAccountName;
    }

    public void setP7_bankAccountName(String p7_bankAccountName) {
        P7_bankAccountName = p7_bankAccountName;
    }

    public String getP8_biz() {
        return P8_biz;
    }

    public void setP8_biz(String p8_biz) {
        P8_biz = p8_biz;
    }

    public String getP9_bankUnionCode() {
        return P9_bankUnionCode;
    }

    public void setP9_bankUnionCode(String p9_bankUnionCode) {
        P9_bankUnionCode = p9_bankUnionCode;
    }

    public String getP10_feeType() {
        return P10_feeType;
    }

    public void setP10_feeType(String p10_feeType) {
        P10_feeType = p10_feeType;
    }

    public String getP11_summary() {
        return P11_summary;
    }

    public void setP11_summary(String p11_summary) {
        P11_summary = p11_summary;
    }
}
