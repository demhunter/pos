/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto;

import com.pos.transaction.dto.request.BindCardDto;
import com.pos.transaction.dto.user.PosUserIdentityDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 快捷收款用户审核信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class PosUserAuditInfoDto implements Serializable {

    private static final long serialVersionUID = 5512984425320651426L;
    @ApiModelProperty("用户身份信息")
    private PosUserIdentityDto identityInfo;

    @ApiModelProperty("用户绑卡信息")
    private BindCardDto bindCardInfo;

    @ApiModelProperty("审核不通过原因")
    private String rejectReason;

    @ApiModelProperty("更新时间-即更新Key，通过比对此字段校验审核数据是否有变动，在审核时需要回传此字段")
    private Date updateKey;

    public Date getUpdateKey() {
        return updateKey;
    }

    public void setUpdateKey(Date updateKey) {
        this.updateKey = updateKey;
    }

    public PosUserIdentityDto getIdentityInfo() {
        return identityInfo;
    }

    public void setIdentityInfo(PosUserIdentityDto identityInfo) {
        this.identityInfo = identityInfo;
    }

    public BindCardDto getBindCardInfo() {
        return bindCardInfo;
    }

    public void setBindCardInfo(BindCardDto bindCardInfo) {
        this.bindCardInfo = bindCardInfo;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
