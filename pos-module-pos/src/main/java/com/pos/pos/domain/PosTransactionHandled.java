/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 手动处理结算信息
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class PosTransactionHandled {

    private Long id; // 自增id

    private Long transactionId; // 处理关联交易记录id

    private BigDecimal amount; // 结算金额

    private Byte payMode; // 打款方式 1=微信，2=支付，3=网银，4=线下

    private String voucher; // 打款凭证

    private String remark; // 备注

    private Date createTime; // 打款日期

    private Long createUserId; // 创建人UserId

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Byte getPayMode() {
        return payMode;
    }

    public void setPayMode(Byte payMode) {
        this.payMode = payMode;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
}
