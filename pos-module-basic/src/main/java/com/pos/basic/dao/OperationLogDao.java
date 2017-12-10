/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.domain.OperationLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 操作日志Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/9
 */
@Repository
public interface OperationLogDao {

    /**
     * 保存操作日志
     *
     * @param log 日志信息
     */
    void save(@Param("log") OperationLog log);
}
