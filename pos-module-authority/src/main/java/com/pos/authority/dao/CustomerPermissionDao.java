/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dao;

import com.pos.authority.domain.CustomerPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 客户权限DAO
 *
 * @author wangbing
 * @version 1.0, 2017/11/24
 */
@Repository
public interface CustomerPermissionDao {

    /**
     * 保存客户权限信息
     *
     * @param permission 权限信息
     */
    void save(CustomerPermission permission);

    /**
     * 获取指定用户的权限信息
     *
     * @param userId 用户id
     * @return 权限信息
     */
    CustomerPermission getPermission(@Param("userId") Long userId);
}
