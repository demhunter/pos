/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户权限领域对象
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
public class CustomerAuthority {

    private Long id;

    private Long userId;

    private Byte get; // 收款功能 1=未启用 2=启用 3==关闭

    private BigDecimal getRate; // 收款费率

    private Integer twitterStatus; // 推客状态 1=未启用 2=启用 3==关闭，限制下面两个推广功能（客户和推客），当推客状态未启用时，功能才能正常使用

    private Byte spread; // 推广发展客下级户功能 1=未启用 2=启用 3==关闭

    private Byte develop; // 推广发展下级推客功能 1=未启用 2=启用 3==关闭

    private Integer auditStatus; // 身份认证状态 0=未提交 1=未审核 2=已通过 3==未通过

    private String rejectReason; // 未通过原因

    private Long bankCardId; // bank_card的主键id，记录用户绑定的收款银行卡

    private String bankCardImage; // 对应收款银行卡的正面照

    private Date createTime;

    private Date updateTime;

    private Long updateUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getGet() {
        return get;
    }

    public void setGet(Byte get) {
        this.get = get;
    }

    public BigDecimal getGetRate() {
        return getRate;
    }

    public void setGetRate(BigDecimal getRate) {
        this.getRate = getRate;
    }

    public Integer getTwitterStatus() {
        return twitterStatus;
    }

    public void setTwitterStatus(Integer twitterStatus) {
        this.twitterStatus = twitterStatus;
    }

    public Byte getSpread() {
        return spread;
    }

    public void setSpread(Byte spread) {
        this.spread = spread;
    }

    public Byte getDevelop() {
        return develop;
    }

    public void setDevelop(Byte develop) {
        this.develop = develop;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Long getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(Long bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getBankCardImage() {
        return bankCardImage;
    }

    public void setBankCardImage(String bankCardImage) {
        this.bankCardImage = bankCardImage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
}
