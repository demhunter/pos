/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.converter;

import com.pos.authority.domain.CustomerPermission;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import org.springframework.beans.BeanUtils;

/**
 * 客户权限关系转化工具
 *
 * @author wangbing
 * @version 1.0, 2017/12/4
 */
public class CustomerPermissionConverter {

    public static CustomerPermissionDto toCustomerPermissionDto(CustomerPermission permission) {
        CustomerPermissionDto dto = new CustomerPermissionDto();

        BeanUtils.copyProperties(permission, dto);

        return dto;
    }
}
