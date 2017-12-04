/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dao;

import com.pos.authority.domain.CustomerPermission;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    List<CustomerPermissionDto> query();
}
