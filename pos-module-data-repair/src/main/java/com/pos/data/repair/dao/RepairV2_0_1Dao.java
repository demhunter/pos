/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.data.repair.dao;

import com.pos.authority.domain.CustomerPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据修复Dao
 *
 * @author wangbing
 * @version 1.0, 2018/1/25
 */
@Repository
public interface RepairV2_0_1Dao {

    /**
     * 根据等级获取指定用户组的权限信息
     *
     * @param level 等级
     * @return 权限信息
     */
    List<CustomerPermission> getPermissionsByLevel(@Param("level") Integer level);

    /**
     * 更新客户权限等级信息
     *
     * @param permission 权限等级信息
     */
    void updateLevelInfo(@Param("permission") CustomerPermission permission);
}
