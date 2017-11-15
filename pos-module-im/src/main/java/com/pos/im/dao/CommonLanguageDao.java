/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.basic.dto.UserIdentifier;
import com.pos.im.domain.CommonLanguage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * IM用户常用语DAO.
 *
 * @author wayne
 * @version 1.0, 2017/5/2
 */
@Repository
public interface CommonLanguageDao {

    CommonLanguage get(@Param("id") Long id);

    void save(@Param("cl") CommonLanguage commonLanguage);

    void update(@Param("cl") CommonLanguage commonLanguage);

    List<CommonLanguage> findLanguagesByUser(@Param("user") UserIdentifier user);

}
