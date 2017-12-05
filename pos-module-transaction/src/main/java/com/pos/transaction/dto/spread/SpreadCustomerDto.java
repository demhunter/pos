/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.spread;

import com.pos.transaction.constants.UserAuditStatus;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 发展的收款客户信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class SpreadCustomerDto implements Serializable {

    private static final long serialVersionUID = 5101116054975171288L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("上线推客的userId")
    private Long channelUserId;

    @ApiModelProperty("下级客户的userId")
    private Long juniorUserId;

    @ApiModelProperty("下级客户姓名")
    private String juniorName;

    @ApiModelProperty("下级客户的电话")
    private String juniorPhone;

    @ApiModelProperty("下级客户的备注")
    private String juniorRemark;

    @ApiModelProperty("上下级关系是否有效，当为false时表示解除上下级推客客户关系")
    private Boolean relationAvailable;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("为推客提供的累计佣金")
    private BigDecimal brokerage;

    @ApiModelProperty("身份认证状态 0=未提交 1=未审核 2=已通过 3==未通过")
    private Integer auditStatus;

    public UserAuditStatus parseAuditStatus() {
        return auditStatus == null ? null : UserAuditStatus.getEnum(auditStatus);
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Long channelUserId) {
        this.channelUserId = channelUserId;
    }

    public Long getJuniorUserId() {
        return juniorUserId;
    }

    public void setJuniorUserId(Long juniorUserId) {
        this.juniorUserId = juniorUserId;
    }

    public String getJuniorName() {
        return juniorName;
    }

    public void setJuniorName(String juniorName) {
        this.juniorName = juniorName;
    }

    public String getJuniorPhone() {
        return juniorPhone;
    }

    public void setJuniorPhone(String juniorPhone) {
        this.juniorPhone = juniorPhone;
    }

    public String getJuniorRemark() {
        return juniorRemark;
    }

    public void setJuniorRemark(String juniorRemark) {
        this.juniorRemark = juniorRemark;
    }

    public Boolean getRelationAvailable() {
        return relationAvailable;
    }

    public void setRelationAvailable(Boolean relationAvailable) {
        this.relationAvailable = relationAvailable;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }
}
