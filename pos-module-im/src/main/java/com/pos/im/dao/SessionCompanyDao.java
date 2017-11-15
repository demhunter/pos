/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.im.domain.SessionCompany;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 与IM会话关联的公司DAO.
 *
 * @author wayne
 * @version 1.0, 2016/9/19
 */
@Repository
public interface SessionCompanyDao {

    void save(@Param("sc") SessionCompany sc);

    void saveBatch(@Param("list") List<SessionCompany> list);

    SessionCompany find(@Param("sessionId") Long sessionId, @Param("companyId") Long companyId);

    List<SessionCompany> findCompanies(
            @Param("sessionId") Long sessionId, @Param("companiesId") List<Long> companiesId);

    /**
     * 以原子方式将指定公司在指定会话中的呼叫次数+1.
     *
     * @param sessionId 会话ID
     * @param companyId 公司ID
     * @return 更新的行数
     */
    int incrementCallTotal(@Param("sessionId") Long sessionId, @Param("companyId") Long companyId);

}
