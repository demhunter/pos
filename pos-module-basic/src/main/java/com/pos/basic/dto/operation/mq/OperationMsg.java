/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.operation.mq;

import com.pos.basic.constant.OperationType;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.validation.FieldChecker;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作消息
 *
 * @author wangbing
 * @version 1.0, 2017/12/9
 */
public class OperationMsg implements Serializable {

    private static final long serialVersionUID = -3365053657028492164L;

    private UserIdentifier operator; // 操作人

    private Integer operationType; // 操作父类型code

    private Integer operationDetailType; // 操作子类型code

    private Boolean operateSucc; // 操作结果：TRUE：操作成功，FALSE：操作失败

    private HashMap<String, Object> failureContent; // 操作失败时，相关操作数据


    public OperationMsg() {
    }

    private OperationMsg(UserIdentifier operator, OperationType.SubOperationType operation) {
        this.operator = operator;
        this.operationType = operation.getParent().getCode();
        this.operationDetailType = operation.getCode();
        this.failureContent = new HashMap<>();
    }

    public static OperationMsg create(UserIdentifier operator, OperationType.SubOperationType operation) {
        return new OperationMsg(operator, operation);
    }

    public void check() {
        FieldChecker.checkEmpty(operator, "operator");
        FieldChecker.checkEmpty(operationType, "operationType");
        FieldChecker.checkEmpty(operationDetailType, "operationDetailType`");
        FieldChecker.checkEmpty(operateSucc, "operateSucc");
        if (!getOperateSucc()) {
            FieldChecker.checkEmpty(failureContent, "failureContent");
        }
    }

    public OperationType.SubOperationType parseSubOperation() {
        return OperationType.getSubOperation(operationType, operationDetailType);
    }

    /**
     * 操作成功
     */
    public void operateSuccess() {
        this.operateSucc = true;
        // 操作成功，清空错误信息
        if (!CollectionUtils.isEmpty(this.failureContent)) {
            this.failureContent.clear();
        }
    }

    /**
     * 操作失败
     */
    public void operateFailure() {
        this.operateSucc = false;
    }

    /**
     * 添加错误相关信息
     *
     * @param requestData 请求数据
     */
    public void addFailureRequestInfo(Object requestData) {
        FieldChecker.checkEmpty(requestData, "requestData");

        this.failureContent.put("request", requestData);
    }

    /**
     * 设置操作失败原因
     *
     * @param failReason 操作失败原因
     */
    public void setFailReason(Object failReason) {
        FieldChecker.checkEmpty(failReason, "failReason");

        this.failureContent.put("failReason", failReason);
    }

    /**
     * 设置操作异常信息
     *
     * @param e 操作异常信息
     */
    public void setException(Exception e) {
        this.failureContent.put("exception", e.toString());
    }

    public UserIdentifier getOperator() {
        return operator;
    }

    public void setOperator(UserIdentifier operator) {
        this.operator = operator;
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

    public Boolean getOperateSucc() {
        return operateSucc;
    }

    public void setOperateSucc(Boolean operateSucc) {
        this.operateSucc = operateSucc;
    }

    public HashMap<String, Object> getFailureContent() {
        return failureContent;
    }

    public void setFailureContent(HashMap<String, Object> failureContent) {
        this.failureContent = failureContent;
    }
}
