/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.spread;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.pos.constants.UserAuditStatus;

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
    private Long twitterUserId;

    @ApiModelProperty("下级客户的userId")
    private Long userId;

    @ApiModelProperty("下级客户姓名")
    private String name;

    @ApiModelProperty("下级客户的电话")
    private String phone;

    @ApiModelProperty("下级客户的备注")
    private String remark;

    @ApiModelProperty("上下级关系是否有效，当为false时表示解除上下级推客客户关系")
    private Boolean available;

    @ApiModelProperty("创建时间")
    private Date createTime;

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

    public Long getTwitterUserId() {
        return twitterUserId;
    }

    public void setTwitterUserId(Long twitterUserId) {
        this.twitterUserId = twitterUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }
}
