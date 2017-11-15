/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session.build;

import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;

/**
 * 根据案例来创建会话的所需信息的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/7/13
 */
public class SessionBuildByCase extends SessionBuildDto implements Serializable {

    private static final long serialVersionUID = 3437747778324245950L;

    private Long caseId; // 会话关联的案例ID

    private String caseName; // 会话关联的案例名称

    private Long companyId; // 会话关联案例所属的公司ID

    private String caseCover; // 作品封面

    @Override
    public void check(String fieldPrefix) {
        super.check(fieldPrefix);
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(caseId, fieldPrefix + "caseId");
        FieldChecker.checkEmpty(caseName, fieldPrefix + "caseName");
        FieldChecker.checkEmpty(companyId, fieldPrefix + "companyId");
        FieldChecker.checkEmpty(caseCover, fieldPrefix + "caseCover");
    }

    @Override
    public String getSessionName() {
        return getCreator().getShowName() + "-" + caseName;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCaseCover() {
        return caseCover;
    }

    public void setCaseCover(String caseCover) {
        this.caseCover = caseCover;
    }
}