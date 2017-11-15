/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.im.domain.CompanyInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * IM公司信息DAO.
 *
 * @author wayne
 * @version 1.0, 2016/11/23
 */
@Repository
public interface CompanyInfoDao {

    void save(@Param("ci") CompanyInfo companyInfo);

    void update(@Param("ci") CompanyInfo companyInfo);

    CompanyInfo findByCompanyId(@Param("companyId") Long companyId);

    /**
     * 查询会话关联的公司信息.
     *
     * @param sessionId 会话ID
     * @return
     */
    List<CompanyInfo> findCompaniesBySessionId(@Param("sessionId") Long sessionId);

}
