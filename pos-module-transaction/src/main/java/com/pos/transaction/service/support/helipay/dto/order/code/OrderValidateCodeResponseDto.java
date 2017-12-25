/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto.order.code;

import com.pos.transaction.service.support.helipay.dto.HelibaoBasicResponse;

import java.io.Serializable;

/**
 * 发送支付验证码请求响应信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/22
 */
public class OrderValidateCodeResponseDto extends HelibaoBasicResponse {

    private String rt1_bizType; // 交易类型，固定值为：QuickPaySendValidateCode

    private String rt2_retCode; // 返回码

    private String rt3_retMsg; // 返回信息，返回0000即成功

    private String rt4_customerNumber; // 商户编号

    private String rt5_orderId; // 商户订单号

    private String rt6_phone; // 手机号码

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

    public String getRt5_orderId() {
        return rt5_orderId;
    }

    public void setRt5_orderId(String rt5_orderId) {
        this.rt5_orderId = rt5_orderId;
    }

    public String getRt6_phone() {
        return rt6_phone;
    }

    public void setRt6_phone(String rt6_phone) {
        this.rt6_phone = rt6_phone;
    }

    @Override
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
