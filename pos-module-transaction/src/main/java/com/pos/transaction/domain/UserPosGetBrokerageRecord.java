/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * POS 推客提取佣金记录
 *
 * @author 睿智
 * @version 1.0, 2017/8/24
 */
public class UserPosGetBrokerageRecord implements Serializable {

    private static final long serialVersionUID = 7235619241081717530L;

    private long id;

    private long userId; // 渠道商的userid

    private BigDecimal amount; // 提取的佣金

    private byte payMode; // 公司的打款方式 1=微信，2=支付，3=网银，4=线下

    private String voucher; // 打款凭证

    private Date payDate; // 打款日期

    private String remark; // 备注

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public byte getPayMode() {
        return payMode;
    }

    public void setPayMode(byte payMode) {
        this.payMode = payMode;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
