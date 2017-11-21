/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.domain.PayArea;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 支付相关地区Dao
 *
 * @author cc
 * @version 1.0, 2016/12/1
 */
@Repository
public interface PayAreaDao {

    /**
     * 查询支付相关区域列表
     *
     * @return 区域列表
     */
    List<PayArea> queryPayAreas();
}
