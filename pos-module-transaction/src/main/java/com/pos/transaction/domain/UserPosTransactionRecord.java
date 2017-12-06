/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.domain;

import com.pos.transaction.dto.PosOutCardInfoDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户pos的交易记录
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class UserPosTransactionRecord implements Serializable {

    private static final long serialVersionUID = 195628922523210462L;

    private Long id;

    private String recordNum;//流水号

    private Integer transactionType; // 交易类型

    private Long inCardId;//转入的卡ID

    /** @see PosOutCardInfoDto */
    private String outCardInfo; // 转出卡信息 JSON

    private Long userId;//提现的人的userId

    private BigDecimal amount;//提现的金额

    private BigDecimal serviceCharge;//手续费，用户提现时所有需要扣除的金额

    private BigDecimal arrivalAmount;//实际到账金额

    private BigDecimal payCharge;//支付手续费（用户发起支付，支付公司到平台扣除的）

    private BigDecimal posCharge;//提现手续费（平台支付给用户时，支付公司扣除的）

    private Integer status;//状态

    private Date createDate;//创建时间

    private Date payDate;//用户支付的时间

    private Date completeDate;//公司支付给用户的时间

    private String helibaoZhifuNum;//合利宝支付的流水号

    private String helibaoTixianNum;//合利宝提现的流水号

    private Integer failureTimes; // 交易失败次数(>0 表示存在过失败情况）

    private Date updateTime; // 交易更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public Long getInCardId() {
        return inCardId;
    }

    public void setInCardId(Long inCardId) {
        this.inCardId = inCardId;
    }

    public String getOutCardInfo() {
        return outCardInfo;
    }

    public void setOutCardInfo(String outCardInfo) {
        this.outCardInfo = outCardInfo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getArrivalAmount() {
        return arrivalAmount;
    }

    public void setArrivalAmount(BigDecimal arrivalAmount) {
        this.arrivalAmount = arrivalAmount;
    }

    public BigDecimal getPayCharge() {
        return payCharge;
    }

    public void setPayCharge(BigDecimal payCharge) {
        this.payCharge = payCharge;
    }

    public BigDecimal getPosCharge() {
        return posCharge;
    }

    public void setPosCharge(BigDecimal posCharge) {
        this.posCharge = posCharge;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getHelibaoZhifuNum() {
        return helibaoZhifuNum;
    }

    public void setHelibaoZhifuNum(String helibaoZhifuNum) {
        this.helibaoZhifuNum = helibaoZhifuNum;
    }

    public String getHelibaoTixianNum() {
        return helibaoTixianNum;
    }

    public void setHelibaoTixianNum(String helibaoTixianNum) {
        this.helibaoTixianNum = helibaoTixianNum;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getFailureTimes() {
        return failureTimes;
    }

    public void setFailureTimes(Integer failureTimes) {
        this.failureTimes = failureTimes;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
