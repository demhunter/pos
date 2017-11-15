/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * BankLogoDao
 *
 * @author cc
 * @version 1.0, 2017/1/6
 */
@Repository
public interface BankLogoDao {

    /**
     * 查询银行logo Map
     *
     * @return 银行名和Logo映射
     */
    List<Map<String, String>> queryBankLogos();
}
