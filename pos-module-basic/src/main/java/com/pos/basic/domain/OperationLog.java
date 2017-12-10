/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import org.slf4j.Logger;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志领域对象
 *
 * @author wangbing
 * @version 1.0, 2017/12/9
 */
public class OperationLog implements Serializable {

    private static final String CUSTOMER_TYPE = "c";
    private static final String MANAGER_TYPE = "m";

    private String uuid; // 操作uuid

    private Long userId; // 操作人id

    private String userType; // 操作人类型

    private Integer operationType; // 操作类型

    private Integer operationDetailType; // 操作详细类型

    private String operationName; // 操作名称

    private Boolean succ; // 操作结果（TRUE：成功；FALSE：失败）

    private String failureContent; // 操作失败原因及相关数据

    private Date createTime; // 操作时间

    public boolean isCustomer() {
        return CUSTOMER_TYPE.equals(userType);
    }

    public boolean isManager() {
        return MANAGER_TYPE.equals(userType);
    }

    public void catalinaOut(final Logger LOG) {
        String logTemplate = "";
        if (this.isManager()) {
            logTemplate = logTemplate + "管理员[{}]";
        } else {
            logTemplate = logTemplate + "用户[{}]";
        }
        if (this.getSucc()) {
            logTemplate = logTemplate + "执行[{}]操作--成功！";
            LOG.info(logTemplate, this.getUserId(), this.getOperationName());
        } else {
            logTemplate = logTemplate + "执行[{}]操作--失败！操作相关信息：{}";
            LOG.info(logTemplate, this.getUserId(), this.getOperationName(), this.getFailureContent());
        }
    }

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
