/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto.order.pay;

/**
 * 订单确认支付信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/22
 */
public class OrderConfirmPayDto {

    private String P1_bizType; // 交易类型，固定值为：QuickPayConfirmPay

    private String P2_customerNumber; // 商户编号

    private String P3_orderId; // 商户订单号

    private String P4_timestamp; // 时间戳，格式：YYYYMMDDH24MISS

    private String P5_validateCode; // 短信验证码

    private String P6_orderIp; // 支付IP，用户支付时使用的网络终端IP

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

    public String getP4_timestamp() {
        return P4_timestamp;
    }

    public void setP4_timestamp(String p4_timestamp) {
        P4_timestamp = p4_timestamp;
    }

    public String getP5_validateCode() {
        return P5_validateCode;
    }

    public void setP5_validateCode(String p5_validateCode) {
        P5_validateCode = p5_validateCode;
    }

    public String getP6_orderIp() {
        return P6_orderIp;
    }

    public void setP6_orderIp(String p6_orderIp) {
        P6_orderIp = p6_orderIp;
    }
}
