/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto.order.code;

import java.io.Serializable;

/**
 * 发送支付验证码请求Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/22
 */
public class OrderValidateCodeDto implements Serializable {
    private static final long serialVersionUID = 2054648354362848167L;

    private String P1_bizType; // 交易类型，固定值为：QuickPaySendValidateCode

    private String P2_customerNumber; // 商户编号

    private String P3_orderId; // 商户订单号

    private String P4_timestamp; // 时间戳，格式：YYYYMMDDH24MISS

    private String P5_phone; // 手机号码

    private String P6_smsSignature; // 短信头或短信签名，如：【鹦鹉收款】您的短信验证码为：*****

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

    public String getP5_phone() {
        return P5_phone;
    }

    public void setP5_phone(String p5_phone) {
        P5_phone = p5_phone;
    }

    public String getP6_smsSignature() {
        return P6_smsSignature;
    }

    public void setP6_smsSignature(String p6_smsSignature) {
        P6_smsSignature = p6_smsSignature;
    }
}
