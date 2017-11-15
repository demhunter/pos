/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.helipay.vo;

/**
 * 提现查询返回结果
 *
 * @author wangbing
 * @version 1.0, 2017/10/19
 */
public class QuerySettlementCardVo {

    private String rt1_bizType; // 交易类型

    private String rt2_retCode; // 返回码

    private String rt3_retMsg; // 返回信息

    private String rt4_customerNumber; // 商户编号

    private String rt5_orderId; // 商户订单号

    private String rt6_serialNumber; // 平台流水号

    private String rt7_orderStatus; // 打款状态

    private String sign; // 签名

    public String getRt1_bizType() {
        return rt1_bizType;
    }

    public void setRt1_bizType(String rt1_bizType) {
        this.rt1_bizType = rt1_bizType;
    }

    public String getRt2_retCode() {
        return rt2_retCode;
    }

    public void setRt2_retCode(String rt2_retCode) {
        this.rt2_retCode = rt2_retCode;
    }

    public String getRt3_retMsg() {
        return rt3_retMsg;
    }

    public void setRt3_retMsg(String rt3_retMsg) {
        this.rt3_retMsg = rt3_retMsg;
    }

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

    public String getRt7_orderStatus() {
        return rt7_orderStatus;
    }

    public void setRt7_orderStatus(String rt7_orderStatus) {
        this.rt7_orderStatus = rt7_orderStatus;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
