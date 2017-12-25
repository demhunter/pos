/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto.settlement.withdraw.query;

/**
 * 结算提现查询信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/25
 */
public class SettlementWithdrawQueryDto {

    private String P1_bizType; // 交易类型，固定值为：TransferQuery

    private String P2_orderId; // 商户订单号

    private String P3_customerNumber; // 商户编号

    public SettlementWithdrawQueryDto() {
    }

    public SettlementWithdrawQueryDto(String p2_orderId) {
        P1_bizType = "TransferQuery";
        P2_orderId = p2_orderId;
    }

    public String getP1_bizType() {
        return P1_bizType;
    }

    public void setP1_bizType(String p1_bizType) {
        P1_bizType = p1_bizType;
    }

    public String getP2_orderId() {
        return P2_orderId;
    }

    public void setP2_orderId(String p2_orderId) {
        P2_orderId = p2_orderId;
    }

    public String getP3_customerNumber() {
        return P3_customerNumber;
    }

    public void setP3_customerNumber(String p3_customerNumber) {
        P3_customerNumber = p3_customerNumber;
    }
}
