/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dao;

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

    List<CustomerPermissionDto> query();
}
