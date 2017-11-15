package com.pos.pos.helipay.vo;

/**
 * Created by heli50 on 2017/4/14.
 */
public class SendValidateCodeVo {
    private String P1_bizType;
    private String P2_customerNumber;
    private String P3_orderId;
    private String P4_timestamp;
    private String P5_phone;

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
