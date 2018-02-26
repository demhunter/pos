/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto.order.withdraw;

/**
 * 结算卡提现信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/25
 */
public class SettlementWithdrawDto {

    private String P1_bizType; // 交易类型，固定值为：SettlementCardWithdraw

    private String P2_customerNumber; // 商户编号

    private String P3_userId; // 用户id

    private String P4_orderId; // 商户订单号

    private String P5_amount; // 订单金额，以元为单位，最小金额为0.01

    private String P6_feeType; // 手续费收取方式[PAYER:付款方收取手续费、RECEIVER:收款方收取手续费]

    private String P7_summary; // 提现备注

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

    public String getP5_amount() {
        return P5_amount;
    }

    public void setP5_amount(String p5_amount) {
        P5_amount = p5_amount;
    }

    public String getP6_feeType() {
        return P6_feeType;
    }

    public void setP6_feeType(String p6_feeType) {
        P6_feeType = p6_feeType;
    }

    public String getP7_summary() {
        return P7_summary;
    }

    public void setP7_summary(String p7_summary) {
        P7_summary = p7_summary;
    }
}
