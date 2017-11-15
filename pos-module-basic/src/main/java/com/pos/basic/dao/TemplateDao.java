/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 模板内容Dao
 *
 * @author cc
 * @version 1.0, 2017/3/7
 */
@Repository
public interface TemplateDao {

    String queryTemplateById(@Param("id") Long id);
}
