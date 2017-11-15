/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service;

import com.pos.im.dto.company.CompanyInfoDto;

import java.util.List;

/**
 * IM公司服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/12/6
 */
public interface IMCompanyService {

    /**
     * 刷新指定公司在IM中保存的信息.
     *
     * @param companyInfoDto 公司信息
     */
    void refresh(CompanyInfoDto companyInfoDto);

    /**
     * 查询会话关联的公司信息.
     *
     * @param sessionId 会话ID
     * @return
     */
    List<CompanyInfoDto> findCompaniesBySessionId(Long sessionId);

}
