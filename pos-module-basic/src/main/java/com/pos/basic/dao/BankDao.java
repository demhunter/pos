/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 银行Dao
 *
 * @author cc
 * @version 1.0, 2016/12/1
 */
@Repository
public interface BankDao {

    /**
     * 查询银行名（顶级）
     *
     * @return 银行名列表
     */
    List<String> queryHeadNames();
}
