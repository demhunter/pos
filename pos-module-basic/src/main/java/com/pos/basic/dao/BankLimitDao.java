/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * BankLimitDao
 *
 * @author cc
 * @version 1.0, 2017/1/9
 */
@Repository
public interface BankLimitDao {

    /**
     * 查询银行限额信息
     *
     * @return 限额 Map
     */
    List<Map<String, Object>> queryBankLimits();
}
