/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.user;

import com.pos.transaction.constants.CardTypeEnum;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.transaction.constants.UserAuditStatus;
import com.pos.transaction.dto.auth.BaseAuthDto;
import com.pos.user.dto.customer.CustomerDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 快捷收款用户聚合信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/8/24
 */
public class PosUserIntegrateDto implements Serializable{

    private static final long serialVersionUID = -6753840503202892871L;
    /***********************快捷收款用户权限信息***************************/
    @ApiModelProperty("自增id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("收款功能权限（int，1 = 未启用 2 = 启用 3 = 关闭）")
    private Byte get;

    @ApiModelProperty("收款费率（BigDecimal，具体数值，如0.0045）")
    private BigDecimal getRate;

    @ApiModelProperty("手续费（BigDecimal，单位：元）")
    private BigDecimal poundage;

    @ApiModelProperty("推客状态（1=未启用 2=启用 3==关闭，限制下面两个推广功能，当推客状态为启用时，功能才能正常使用）")
    private Integer twitterStatus;

    @ApiModelProperty("推广功能权限（int，1 = 未启用 2 = 启用 3 = 关闭）")
    private Byte spread;

    @ApiModelProperty("发展渠道商权限（int，1 = 未启用 2 = 启用 3 = 关闭）")
    private Byte develop;

    @ApiModelProperty("快捷收款用户注册时间（Date）")
    private Date createDate;

    @ApiModelProperty("身份认证审核状态：0 = 未提交，1 = 未审核，2 = 已通过，3 = 未通过")
    private Integer auditStatus;

    /***********************快捷收款--收款银行卡信息***************************/
    @ApiModelProperty("绑定的收款银行卡id")
    private Long cardId;

    @ApiModelProperty("银行卡号")
    private String bankCardNO;

    @ApiModelProperty("客户姓名-持卡人姓名")
    private String bankCardName;

    @ApiModelProperty("身份证号-持卡人身份证号码")
    private String bankCardIdCardNO;

    @ApiModelProperty("手机号-银行预留手机号")
    private String mobilePhone;

    @ApiModelProperty("银行名字-银行卡所属银行名称")
    private String bankName;

    @ApiModelProperty("银行的代码")
    private String bankCode;

    /** @see CardTypeEnum#code */
    @ApiModelProperty("卡类型：1 = 储蓄卡，2 = 信用卡")
    private Byte cardType;

    /***********************快捷收款--收款信息***************************/
    @ApiModelProperty("收款笔数")
    private Integer userPosCount;

    @ApiModelProperty("收款总金额（BigDecimal）")
    private BigDecimal userPosAmount;

    /***********************快捷收款--推客信息***************************/
    @ApiModelProperty("上线推客用户ID")
    private Long parentUserId;

    @ApiModelProperty("推客用户ID（=userId）")
    private Long channelUserId;

    @ApiModelProperty("推客电话")
    private String channelPhone;

    @ApiModelProperty("备注")
    private String channelRemark;

    @ApiModelProperty("关联时间（Date）")
    private Date relationTime;

    @ApiModelProperty("累计已提现金额（BigDecimal）")
    private BigDecimal totalWithdrawDeposit;

    @ApiModelProperty("当前申请提现金额（BigDecimal）")
    private BigDecimal currentWithdrawDeposit;

    /***********************快捷收款用户--其它信息***************************/
    @ApiModelProperty("快捷用户基本信息")
    private CustomerDto posUserInfo;

    public BaseAuthDto buildBaseAuthDto() {
        BaseAuthDto baseAuth = new BaseAuthDto();

        baseAuth.setGet(Integer.valueOf(get));
        baseAuth.setGetRate(getRate);
        baseAuth.setPoundage(poundage);
        baseAuth.setTwitterStatus(twitterStatus);
        baseAuth.setSpread(Integer.valueOf(spread));
        baseAuth.setDevelop(Integer.valueOf(develop));

        return baseAuth;
    }

    @ApiModelProperty("* 身份认证审核状态描述")
    public String getAuditStatusDesc() {
        return auditStatus == null ? "" : UserAuditStatus.getEnum(auditStatus).getDesc();
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public BigDecimal getGetRate() {
        return getRate;
    }

    public void setGetRate(BigDecimal getRate) {
        this.getRate = getRate;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public Integer getTwitterStatus() {
        return twitterStatus;
    }

    public void setTwitterStatus(Integer twitterStatus) {
        this.twitterStatus = twitterStatus;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getBankCardNO() {
        return bankCardNO;
    }

    public void setBankCardNO(String bankCardNO) {
        this.bankCardNO = bankCardNO;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getBankCardIdCardNO() {
        return bankCardIdCardNO;
    }

    public void setBankCardIdCardNO(String bankCardIdCardNO) {
        this.bankCardIdCardNO = bankCardIdCardNO;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Byte getCardType() {
        return cardType;
    }

    public void setCardType(Byte cardType) {
        this.cardType = cardType;
    }

    public Integer getUserPosCount() {
        return userPosCount;
    }

    public void setUserPosCount(Integer userPosCount) {
        this.userPosCount = userPosCount;
    }

    public BigDecimal getUserPosAmount() {
        return userPosAmount;
    }

    public void setUserPosAmount(BigDecimal userPosAmount) {
        this.userPosAmount = userPosAmount;
    }

    public CustomerDto getPosUserInfo() {
        return posUserInfo;
    }

    public void setPosUserInfo(CustomerDto posUserInfo) {
        this.posUserInfo = posUserInfo;
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

    public Byte getGet() {
        return get;
    }

    public void setGet(Byte get) {
        this.get = get;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public Long getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Long channelUserId) {
        this.channelUserId = channelUserId;
    }

    public String getChannelPhone() {
        return channelPhone;
    }

    public void setChannelPhone(String channelPhone) {
        this.channelPhone = channelPhone;
    }

    public String getChannelRemark() {
        return channelRemark;
    }

    public void setChannelRemark(String channelRemark) {
        this.channelRemark = channelRemark;
    }

    public Date getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(Date relationTime) {
        this.relationTime = relationTime;
    }

    public BigDecimal getTotalWithdrawDeposit() {
        return totalWithdrawDeposit;
    }

    public void setTotalWithdrawDeposit(BigDecimal totalWithdrawDeposit) {
        this.totalWithdrawDeposit = totalWithdrawDeposit;
    }

    public BigDecimal getCurrentWithdrawDeposit() {
        return currentWithdrawDeposit;
    }

    public void setCurrentWithdrawDeposit(BigDecimal currentWithdrawDeposit) {
        this.currentWithdrawDeposit = currentWithdrawDeposit;
    }
}
