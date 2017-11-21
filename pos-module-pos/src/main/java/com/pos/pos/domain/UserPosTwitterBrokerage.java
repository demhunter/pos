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
public class UserPosTwitterBrokerage {

    private Long id;

    private Long recordId; // 交易记录id

    private BigDecimal baseRate; // 基准费率（固定全局配置，万分之五十八）

    private Long agentUserId; // 用户的上级推客userId

    private BigDecimal agentRate; // 上级推客的佣金费率

    private BigDecimal agentCharge; // 用户提现时，平台支付给用户的上级推客的佣金

    private Byte getAgent; // 推客提取佣金的状态 0=未提取 1=已申请 2=已提取

    private Date getAgentDate; // 推客提取佣金的时间

    private Long parentAgentUserId; // 推客的父推客的userId

    private BigDecimal parentAgentRate; // 推客的父推客的佣金费率（固定全局配置，万分之二）

    private BigDecimal parentAgentCharge; // 用户提现时，平台支付给推客的父推客的佣金

    private Byte getParentAgent; // 推客的父推客提取佣金的状态 0=未提取 1=已申请 2=已提取

    private Date getParentDate; // 推客的父推客提取佣金的时间

    private Date createDate; // 创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public BigDecimal getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }

    public Long getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(Long agentUserId) {
        this.agentUserId = agentUserId;
    }

    public BigDecimal getAgentRate() {
        return agentRate;
    }

    public void setAgentRate(BigDecimal agentRate) {
        this.agentRate = agentRate;
    }

    public BigDecimal getAgentCharge() {
        return agentCharge;
    }

    public void setAgentCharge(BigDecimal agentCharge) {
        this.agentCharge = agentCharge;
    }

    public Byte getGetAgent() {
        return getAgent;
    }

    public void setGetAgent(Byte getAgent) {
        this.getAgent = getAgent;
    }

    public Date getGetAgentDate() {
        return getAgentDate;
    }

    public void setGetAgentDate(Date getAgentDate) {
        this.getAgentDate = getAgentDate;
    }

    public Long getParentAgentUserId() {
        return parentAgentUserId;
    }

    public void setParentAgentUserId(Long parentAgentUserId) {
        this.parentAgentUserId = parentAgentUserId;
    }

    public BigDecimal getParentAgentRate() {
        return parentAgentRate;
    }

    public void setParentAgentRate(BigDecimal parentAgentRate) {
        this.parentAgentRate = parentAgentRate;
    }

    public BigDecimal getParentAgentCharge() {
        return parentAgentCharge;
    }

    public void setParentAgentCharge(BigDecimal parentAgentCharge) {
        this.parentAgentCharge = parentAgentCharge;
    }

    public Byte getGetParentAgent() {
        return getParentAgent;
    }

    public void setGetParentAgent(Byte getParentAgent) {
        this.getParentAgent = getParentAgent;
    }

    public Date getGetParentDate() {
        return getParentDate;
    }

    public void setGetParentDate(Date getParentDate) {
        this.getParentDate = getParentDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
