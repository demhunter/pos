/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.domain.DemoDomain;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * DemoEnumDao
 *
 * @author cc
 * @version 1.0, 2017/3/24
 */
@Repository
public interface DemoEnumDao {

    void demoSave(@Param("demo") DemoDomain demo);

    DemoDomain demoQuery(@Param("id") Long id);
}
