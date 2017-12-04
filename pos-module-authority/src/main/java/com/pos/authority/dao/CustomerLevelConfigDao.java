/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dao;

import com.pos.authority.domain.CustomerLevelConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户权限配置Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/4
 */
@Repository
public interface CustomerLevelConfigDao {

    /**
     * 获取的用户等级配置
     *
     * @return 用户等级配置列表
     */
    List<CustomerLevelConfig> getConfigs();

    /**
     * 获取指定等级的配置信息
     *
     * @param level 等级数
     * @return 等级配置信息
     */
    CustomerLevelConfig getLevelConfig(@Param("level") int level);
}
