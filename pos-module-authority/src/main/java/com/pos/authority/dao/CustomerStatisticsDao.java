/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dao;

import com.pos.authority.domain.CustomerStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 客户统计信息Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/4
 */
@Repository
public interface CustomerStatisticsDao {

    /**
     * 保存客户统计信息
     *
     * @param statistics 客户统计信息
     */
    void save(@Param("statistics") CustomerStatistics statistics);

    /**
     * 以原子形式增加用户的直接下级客户数量（+ 1）
     *
     * @param userId 用户id
     */
    void incrementChildrenCount(@Param("userId") Long userId);
}
