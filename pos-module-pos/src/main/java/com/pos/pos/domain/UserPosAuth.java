/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * POS 用户相关的功能权限和认证信息
 *
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class UserPosAuth implements Serializable {

    private static final long serialVersionUID = 4940209107081219248L;

    private Long id;

    private Long userId;

    private Byte get; // 收款功能 1=未启用 2=启用 3==关闭

    private BigDecimal getRate; // 收款费率

    private Integer twitterStatus; // 推客状态 1=未启用 2=启用 3==关闭，限制下面两个推广功能（客户和推客），当推客状态未启用时，功能才能正常使用

    private Byte spread; // 推广发展客下级户功能 1=未启用 2=启用 3==关闭

    private Byte develop; // 推广发展下级推客功能 1=未启用 2=启用 3==关闭

    private Integer auditStatus; // 身份认证状态 0=未提交 1=未审核 2=已通过 3==未通过

    private String rejectReason; // 未通过原因

    private String idCardName; // 真实姓名

    private String idCardNo; // 身份证号码

    private String idImageA; // 身份证正面照

    private String idImageB; // 身份证反面照

    private String idHoldImage; // 身份证正面持证照

    private Long posCardId; // user_pos_card的主键id，记录用户绑定的收款银行卡

    private String posCardImage; // 对应收款银行卡的正面照

    private Date createDate;

    private Date updateDate;

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

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdImageA() {
        return idImageA;
    }

    public void setIdImageA(String idImageA) {
        this.idImageA = idImageA;
    }

    public String getIdImageB() {
        return idImageB;
    }

    public void setIdImageB(String idImageB) {
        this.idImageB = idImageB;
    }

    public String getIdHoldImage() {
        return idHoldImage;
    }

    public void setIdHoldImage(String idHoldImage) {
        this.idHoldImage = idHoldImage;
    }

    public Long getPosCardId() {
        return posCardId;
    }

    public void setPosCardId(Long posCardId) {
        this.posCardId = posCardId;
    }

    public String getPosCardImage() {
        return posCardImage;
    }

    public void setPosCardImage(String posCardImage) {
        this.posCardImage = posCardImage;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
}
