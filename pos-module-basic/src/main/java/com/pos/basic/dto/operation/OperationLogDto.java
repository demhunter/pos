/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.operation;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/9
 */
public class OperationLogDto implements Serializable {

    @ApiModelProperty("操作uuid")
    private String uuid;

    @ApiModelProperty("操作人id")
    private Long userId;

    @ApiModelProperty("操作人类型")
    private String operatorUserType;

    @ApiModelProperty("操作类型")
    private Integer operationType;

    @ApiModelProperty("操作详细类型")
    private Integer operationDetailType;

    @ApiModelProperty("操作名称")
    private String operationName;

    @ApiModelProperty("操作结果（TRUE：成功；FALSE：失败）")
    private Boolean succ;

    @ApiModelProperty("操作失败原因及相关数据")
    private String failureContent;

    @ApiModelProperty("操作时间")
    private Date createTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOperatorUserType() {
        return operatorUserType;
    }

    public void setOperatorUserType(String operatorUserType) {
        this.operatorUserType = operatorUserType;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public Integer getOperationDetailType() {
        return operationDetailType;
    }

    public void setOperationDetailType(Integer operationDetailType) {
        this.operationDetailType = operationDetailType;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Boolean getSucc() {
        return succ;
    }

    public void setSucc(Boolean succ) {
        this.succ = succ;
    }

    public String getFailureContent() {
        return failureContent;
    }

    public void setFailureContent(String failureContent) {
        this.failureContent = failureContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
