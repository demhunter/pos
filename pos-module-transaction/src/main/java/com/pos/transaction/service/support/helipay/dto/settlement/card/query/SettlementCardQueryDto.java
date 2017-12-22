/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto.settlement.card.query;

import com.pos.common.util.date.SimpleDateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 结算卡查询Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/21
 */
public class SettlementCardQueryDto implements Serializable {
    private static final long serialVersionUID = 4792712816836675894L;

    private String P1_bizType; // 交易类型，固定值为：SettlementCardQuery

    private String P2_customerNumber; // 商户编号

    private String P3_userId; // 用户id

    private String P4_orderId; // 订单号，即绑结算卡订单号

    private String P5_timestamp; // 时间戳，格式：yyyyMMddHHmmss（14 位数字，精确到秒）

    public SettlementCardQueryDto() {
        this.P1_bizType = "SettlementCardQuery";
        this.P5_timestamp = SimpleDateUtils.formatDate(
                new Date(), SimpleDateUtils.DatePattern.YYYYMMDDHHMMSS.toString());
    }

    // 修改时间戳
    public void modifyP5_timestamp(String p5_timestamp) {
        this.P5_timestamp = p5_timestamp;
    }

    public String getP1_bizType() {
        return P1_bizType;
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

    public String getP5_timestamp() {
        return P5_timestamp;
    }
}
