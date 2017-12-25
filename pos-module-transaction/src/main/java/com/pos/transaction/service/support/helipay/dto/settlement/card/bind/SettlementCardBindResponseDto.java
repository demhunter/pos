/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto.settlement.card.bind;

import com.pos.transaction.service.support.helipay.dto.HelibaoBasicResponse;

/**
 * 结算绑定返回信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/19
 */
public class SettlementCardBindResponseDto extends HelibaoBasicResponse {

    private String rt1_bizType; // 交易类型，固定值为：[SettlementCardWithdraw]

    private String rt2_retCode; // 返回码

    private String rt3_retMsg; // 返回信息，[不参与签名]

    private String rt4_customerNumber; // 商户编号

    private String rt5_userId; // 用户id

    private String rt6_orderId; // 商户订单号

    private String rt7_bindStatus; // 绑卡状态，[INIT:初始化；SUCCESS:成功；FAIL:失败；DOING:处理中；UNBIND:解绑]

    private String rt8_bankId; // 银行编码

    private String rt9_cardAfterFour; // 银行卡后四位数字

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

    public String getRt6_orderId() {
        return rt6_orderId;
    }

    public void setRt6_orderId(String rt6_orderId) {
        this.rt6_orderId = rt6_orderId;
    }

    public String getRt7_bindStatus() {
        return rt7_bindStatus;
    }

    public void setRt7_bindStatus(String rt7_bindStatus) {
        this.rt7_bindStatus = rt7_bindStatus;
    }

    public String getRt8_bankId() {
        return rt8_bankId;
    }

    public void setRt8_bankId(String rt8_bankId) {
        this.rt8_bankId = rt8_bankId;
    }

    public String getRt9_cardAfterFour() {
        return rt9_cardAfterFour;
    }

    public void setRt9_cardAfterFour(String rt9_cardAfterFour) {
        this.rt9_cardAfterFour = rt9_cardAfterFour;
    }

    @Override
    public String getSign() {
        return sign;
    }
}
