/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * POS 用户提现时需要支付给推客和推客的父推客的佣金信息
 *
 * @author wangbing
 * @version 1.0, 2017/10/12
 */
public class TwitterBrokerage {

    private Long id;

    private Long transactionId; // 交易记录id

    private BigDecimal baseRate; // 基准费率（固定全局配置，万分之五十八）

    private Long twitterUserId; // 推客userId

    private BigDecimal twitterBrokerageRate; // 推客的佣金费率

    private BigDecimal twitterBrokerage; // 用户提现时，平台支付给推客的佣金

    private Byte twitterBrokerageStatus; // 推客提取佣金的状态 0=未提取 1=已申请 2=已提取

    private Date twitterBrokerageStatusTime; // 对应推客佣金状态的变更时间

    private Long parentUserId; // 父推客的userId

    private BigDecimal parentBrokerageRate; // 父推客的佣金费率（固定全局配置，万分之二）

    private BigDecimal parentBrokerage; // 用户提现时，平台支付给父推客的佣金

    private Byte parentBrokerageStatus; // 父推客提取佣金的状态 0=未提取 1=已申请 2=已提取

    private Date parentBrokerageStatusTime; // 对应父推客佣金状态的变更时间

    private Date createTime; // 创建时间

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

    public BigDecimal getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }

    public Long getTwitterUserId() {
        return twitterUserId;
    }

    public void setTwitterUserId(Long twitterUserId) {
        this.twitterUserId = twitterUserId;
    }

    public BigDecimal getTwitterBrokerageRate() {
        return twitterBrokerageRate;
    }

    public void setTwitterBrokerageRate(BigDecimal twitterBrokerageRate) {
        this.twitterBrokerageRate = twitterBrokerageRate;
    }

    public BigDecimal getTwitterBrokerage() {
        return twitterBrokerage;
    }

    public void setTwitterBrokerage(BigDecimal twitterBrokerage) {
        this.twitterBrokerage = twitterBrokerage;
    }

    public Byte getTwitterBrokerageStatus() {
        return twitterBrokerageStatus;
    }

    public void setTwitterBrokerageStatus(Byte twitterBrokerageStatus) {
        this.twitterBrokerageStatus = twitterBrokerageStatus;
    }

    public Date getTwitterBrokerageStatusTime() {
        return twitterBrokerageStatusTime;
    }

    public void setTwitterBrokerageStatusTime(Date twitterBrokerageStatusTime) {
        this.twitterBrokerageStatusTime = twitterBrokerageStatusTime;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public BigDecimal getParentBrokerageRate() {
        return parentBrokerageRate;
    }

    public void setParentBrokerageRate(BigDecimal parentBrokerageRate) {
        this.parentBrokerageRate = parentBrokerageRate;
    }

    public BigDecimal getParentBrokerage() {
        return parentBrokerage;
    }

    public void setParentBrokerage(BigDecimal parentBrokerage) {
        this.parentBrokerage = parentBrokerage;
    }

    public Byte getParentBrokerageStatus() {
        return parentBrokerageStatus;
    }

    public void setParentBrokerageStatus(Byte parentBrokerageStatus) {
        this.parentBrokerageStatus = parentBrokerageStatus;
    }

    public Date getParentBrokerageStatusTime() {
        return parentBrokerageStatusTime;
    }

    public void setParentBrokerageStatusTime(Date parentBrokerageStatusTime) {
        this.parentBrokerageStatusTime = parentBrokerageStatusTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
