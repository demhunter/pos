/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.demo;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author wangbing
 * @version 1.0, 2017/11/6
 */
@Repository
public interface DemoTransactionExceptionDao {

    void save(@Param("demoUser") DemoUser demoUser);
}
