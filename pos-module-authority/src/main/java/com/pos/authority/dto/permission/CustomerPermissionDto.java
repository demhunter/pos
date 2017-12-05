/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dto.permission;

import com.pos.authority.constant.CustomerAuditStatus;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户权限详细DTO
 *
 * @author wangbing
 * @version 1.0, 2017/11/24
 */
public class CustomerPermissionDto extends CustomerPermissionBasicDto implements Serializable {

    private static final long serialVersionUID = 2566400720013890423L;

    @ApiModelProperty("身份认证状态")
    private Integer auditStatus;

    @ApiModelProperty("身份认证审核未通过原因")
    private String rejectReason;

    @ApiModelProperty("真实姓名")
    private String idCardName;

    @ApiModelProperty("身份证号码")
    private String idCardNo;

    @ApiModelProperty("身份证正面照")
    private String idImageA;

    @ApiModelProperty("身份证反面照")
    private String idImageB;

    @ApiModelProperty("绑定的收款银行卡id")
    private Long posCardId;

    @ApiModelProperty("绑定的收款银行卡正面照")
    private String posCardImage;

    @ApiModelProperty("更新操作人id")
    private Long updateUserId;

    @ApiModelProperty("更新操作时间")
    private Date updateTime;

    @ApiModelProperty("权限创建时间")
    private Date createTime;

    public CustomerAuditStatus parseAuditStatus() {
        return auditStatus == null ? null : CustomerAuditStatus.getEnum(auditStatus);
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
