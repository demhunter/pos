/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.auth;

import com.pos.pos.constants.UserAuditStatus;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.service.SecurityService;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.dto.user.PosUserIdentityDto;

import java.io.Serializable;
import java.util.Date;

/**
 * 快捷收款用户权限Dto
 *
 * @author wangbing
 * @version 1.0, 2017/8/24
 */
public class AuthorityDto extends BaseAuthDto implements Serializable {

    @ApiModelProperty("自增id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("身份认证状态 0=未提交 1=未审核 2=已通过 3==未通过")
    private Integer auditStatus;

    @ApiModelProperty("未通过原因")
    private String rejectReason;

    @ApiModelProperty("真实姓名")
    private String idCardName;

    @ApiModelProperty("身份证号码")
    private String idCardNo;

    @ApiModelProperty("身份证正面照")
    private String idImageA;

    @ApiModelProperty("身份证反面照")
    private String idImageB;

    @ApiModelProperty("身份证正面持证照")
    private String idHoldImage;

    @ApiModelProperty("user_pos_card的主键id，记录用户绑定的收款银行卡")
    private Long posCardId;

    @ApiModelProperty("对应收款银行卡的正面照")
    private String posCardImage;

    @ApiModelProperty("快捷收款用户注册时间（Date，更新用户权限时不填此字段）")
    private Date createDate;

    @ApiModelProperty("更新时间")
    private Date updateDate;

    @ApiModelProperty("更新操作人userId")
    private Long updateUserId;

    /**
     * 更新快捷收款用户权限时，校验相关字段
     *
     * @param fieldPrefix 异常提示前缀
     * @throws IllegalParamException 缺少必要参数，或参数值异常
     */
    public void check(String fieldPrefix) throws IllegalParamException {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(id, fieldPrefix + "id");
    }

    public UserAuditStatus parseAuditStatus() {
        return auditStatus == null ? null : UserAuditStatus.getEnum(auditStatus);
    }

    public PosUserIdentityDto buildPosUserIdentity(boolean decrypted, SecurityService securityService) {
        PosUserIdentityDto identity = new PosUserIdentityDto();
        identity.setRealName(getIdCardName());
        identity.setIdCardNo(getIdCardNo());
        if (decrypted) {
            identity.setRealName(securityService.decryptData(identity.getRealName()));
            identity.setIdCardNo(securityService.decryptData(identity.getIdCardNo()));
        }
        identity.setIdImageA(getIdImageA());
        identity.setIdImageB(getIdImageB());
        identity.setIdHoldImage(getIdHoldImage());
        if (UserAuditStatus.AUDITED.equals(parseAuditStatus())
                || UserAuditStatus.NOT_AUDIT.equals(parseAuditStatus())) {
            // 已通过和未审核状态均不允许修改
            identity.setRealNameCanModify(Boolean.FALSE);
            identity.setIdCardNoCanModify(Boolean.FALSE);
            identity.setImageCanModify(Boolean.FALSE);
        } else if (UserAuditStatus.REJECTED.equals(parseAuditStatus())) {
            // 未通过只允许更改身份证照片
            identity.setRealNameCanModify(Boolean.FALSE);
            identity.setIdCardNoCanModify(Boolean.FALSE);
            identity.setImageCanModify(Boolean.TRUE);
        } else {
            // 未提交均允许修改
            identity.setRealNameCanModify(Boolean.TRUE);
            identity.setIdCardNoCanModify(Boolean.TRUE);
            identity.setImageCanModify(Boolean.TRUE);
        }
        return identity;
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
