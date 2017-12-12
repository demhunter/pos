/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.domain;

import com.pos.authority.constant.CustomerAuditStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户权限信息
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
public class CustomerPermission implements Serializable {

    private static final long serialVersionUID = 5973965509689270719L;

    private Long id; // 自增主键id

    private Long userId; // 用户id

    private Integer level; // 用户等级

    private BigDecimal withdrawRate; // 用户提现费率

    private BigDecimal extraServiceCharge; // 用户提现额外手续费

    private Integer auditStatus; // 身份认证状态 0=未提交 1=未审核 2=已通过 3==未通过

    private String rejectReason; // 未通过原因

    private String idCardName; // 真实姓名

    private String idCardNo; // 身份证号码

    private String idImageA; // 身份证正面照

    private String idImageB; // 身份证反面照

    private Long posCardId; // user_pos_card的主键id，记录绑定收款银行卡的信息

    private String posCardImage; // 对应收款银行卡的正面照

    private Long updateUserId; // 更新操作人id

    private Date updateTime; // 更新操作时间

    private Date createTime; // 创建时间

    public CustomerPermission() {
    }

    public CustomerPermission(Long userId, CustomerLevelConfig config) {
        this(userId, config, CustomerAuditStatus.NOT_SUBMIT);
    }

    public CustomerPermission(Long userId, CustomerLevelConfig config, CustomerAuditStatus auditStatus) {
        this.userId = userId;
        this.level = config.getLevel();
        this.withdrawRate = config.getWithdrawRate();
        this.extraServiceCharge = config.getExtraServiceCharge();
        this.auditStatus = auditStatus.getCode();
    }

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BigDecimal getWithdrawRate() {
        return withdrawRate;
    }

    public void setWithdrawRate(BigDecimal withdrawRate) {
        this.withdrawRate = withdrawRate;
    }

    public BigDecimal getExtraServiceCharge() {
        return extraServiceCharge;
    }

    public void setExtraServiceCharge(BigDecimal extraServiceCharge) {
        this.extraServiceCharge = extraServiceCharge;
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

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
