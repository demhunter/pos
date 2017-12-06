/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.converter;

import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.domain.CustomerPermission;
import com.pos.authority.dto.identity.CustomerIdentityDto;
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

    public static CustomerIdentityDto buildCustomerIdentity(CustomerPermissionDto permission) {
        CustomerIdentityDto identity = new CustomerIdentityDto();

        identity.setRealName(permission.getIdCardName());
        identity.setIdCardNo(permission.getIdCardNo());
        identity.setIdImageA(permission.getIdImageA());
        identity.setIdImageB(permission.getIdImageB());

        CustomerAuditStatus auditStatus = permission.parseAuditStatus();
        if (CustomerAuditStatus.REJECTED.equals(auditStatus)) {
            // 未通过只允许更改身份证照片
            identity.setImageCanModify(Boolean.TRUE);
        } else if (CustomerAuditStatus.NOT_SUBMIT.equals(auditStatus)) {
            // 未提交均允许修改
            identity.setRealNameCanModify(Boolean.TRUE);
            identity.setIdCardNoCanModify(Boolean.TRUE);
            identity.setImageCanModify(Boolean.TRUE);
        }

        return identity;
    }
}
