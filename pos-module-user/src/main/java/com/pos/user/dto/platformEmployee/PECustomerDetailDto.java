/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.pos.user.constant.CustomerOrderStatus;
import com.pos.user.dto.customer.CustomerDto;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 家居顾问客户身份详细信息
 *
 * @author wangbing
 * @version 1.0, 2017/7/6
 */
public class PECustomerDetailDto extends CustomerDto implements Serializable {

    @ApiModelProperty("关联关系id")
    private Long relationId;

    @ApiModelProperty("家居顾问的userId")
    private Long peUserId;

    @ApiModelProperty("客户的userId")
    private Long cUserId;

    @ApiModelProperty("备注")
    private String remark;

    /**
     * @see CustomerOrderStatus
     */
    @ApiModelProperty("客户状态（1：谈单中，2：已成单，3：已飞单，4：已完结）")
    private Byte status;

    @ApiModelProperty("关联的渠道")
    private byte relationType;

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Long getPeUserId() {
        return peUserId;
    }

    public void setPeUserId(Long peUserId) {
        this.peUserId = peUserId;
    }

    public Long getcUserId() {
        return cUserId;
    }

    public void setcUserId(Long cUserId) {
        this.cUserId = cUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public byte getRelationType() {
        return relationType;
    }

    public void setRelationType(byte relationType) {
        this.relationType = relationType;
    }
}
