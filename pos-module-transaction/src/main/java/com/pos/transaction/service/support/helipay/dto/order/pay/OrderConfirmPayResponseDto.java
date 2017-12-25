/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto.order.pay;

import com.pos.transaction.service.support.helipay.dto.HelibaoBasicResponse;

import java.io.Serializable;

/**
 * 订单确认支付响应信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/22
 */
public class OrderConfirmPayResponseDto extends HelibaoBasicResponse implements Serializable {

    private static final long serialVersionUID = -5209581677057345180L;

    private String rt1_bizType; // 交易类型，固定值为：QuickPayConfirmPay

    private String rt2_retCode; // 返回码，返回0000即为成功

    private String rt3_retMsg; // 返回信息

    private String rt4_customerNumber; // 商户编号

    private String rt5_orderId; // 商户订单号

    private String rt6_serialNumber; // 合利宝交易流水号

    private String rt7_completeDate; // 订单完成时间，格式：yyyy-MM-dd HH:mm:ss

    private String rt8_orderAmount; // 订单金额

    private String rt9_orderStatus; // 订单状态，[INIT:未支付、SUCCESS：成功、CANCELLED：已取消、REFUNDED：已退款、FAILED：失败、DOING：处理中]

    private String rt10_bindId; // 绑卡Id

    private String rt11_bankId; // 银行编码

    private String rt12_onlineCardType; // 银行卡类型，[DEBIT:借记卡、CREDIT:信用卡]

    private String rt13_cardAfterFour; // 银行卡后四位

    private String rt14_userId; // 用户标识

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

    public String getRt6_serialNumber() {
        return rt6_serialNumber;
    }

    public void setRt6_serialNumber(String rt6_serialNumber) {
        this.rt6_serialNumber = rt6_serialNumber;
    }

    public String getRt7_completeDate() {
        return rt7_completeDate;
    }

    public void setRt7_completeDate(String rt7_completeDate) {
        this.rt7_completeDate = rt7_completeDate;
    }

    public String getRt8_orderAmount() {
        return rt8_orderAmount;
    }

    public void setRt8_orderAmount(String rt8_orderAmount) {
        this.rt8_orderAmount = rt8_orderAmount;
    }

    public String getRt9_orderStatus() {
        return rt9_orderStatus;
    }

    public void setRt9_orderStatus(String rt9_orderStatus) {
        this.rt9_orderStatus = rt9_orderStatus;
    }

    public String getRt10_bindId() {
        return rt10_bindId;
    }

    public void setRt10_bindId(String rt10_bindId) {
        this.rt10_bindId = rt10_bindId;
    }

    public String getRt11_bankId() {
        return rt11_bankId;
    }

    public void setRt11_bankId(String rt11_bankId) {
        this.rt11_bankId = rt11_bankId;
    }

    public String getRt12_onlineCardType() {
        return rt12_onlineCardType;
    }

    public void setRt12_onlineCardType(String rt12_onlineCardType) {
        this.rt12_onlineCardType = rt12_onlineCardType;
    }

    public String getRt13_cardAfterFour() {
        return rt13_cardAfterFour;
    }

    public void setRt13_cardAfterFour(String rt13_cardAfterFour) {
        this.rt13_cardAfterFour = rt13_cardAfterFour;
    }

    public String getRt14_userId() {
        return rt14_userId;
    }

    public void setRt14_userId(String rt14_userId) {
        this.rt14_userId = rt14_userId;
    }

    @Override
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
