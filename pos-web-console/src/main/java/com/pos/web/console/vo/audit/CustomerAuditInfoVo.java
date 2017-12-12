/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.vo.audit;

import com.pos.authority.dto.identity.CustomerIdentityDto;
import com.pos.transaction.dto.request.BindCardDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户被审核信息VO
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
public class CustomerAuditInfoVo implements Serializable {

    private static final long serialVersionUID = -469509669766901038L;

    @ApiModelProperty("用户身份信息")
    private CustomerIdentityDto identityInfo;

    @ApiModelProperty("用户绑卡信息")
    private BindCardDto bindCardInfo;

    @ApiModelProperty("审核不通过原因")
    private String rejectReason;

    @ApiModelProperty("更新时间-即更新Key，通过比对此字段校验审核数据是否有变动，在审核时需要回传此字段")
    private Date updateKey;

    public CustomerIdentityDto getIdentityInfo() {
        return identityInfo;
    }

    public void setIdentityInfo(CustomerIdentityDto identityInfo) {
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

    public Date getUpdateKey() {
        return updateKey;
    }

    public void setUpdateKey(Date updateKey) {
        this.updateKey = updateKey;
    }
}
