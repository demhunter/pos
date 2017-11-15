/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.converter;

import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.im.dto.user.UserInfoDto;
import com.pos.user.dto.manager.ManagerDto;
import com.pos.user.constant.EmployeeType;
import com.pos.user.constant.ManagerType;
import com.pos.user.dto.employee.EmployeeDto;
import com.pos.user.dto.merchant.MerchantDto;
import com.pos.user.dto.UserDto;

/**
 * 用户IM信息的DTO转换器.
 *
 * @author wayne
 * @version 1.0, 2016/8/9
 */
public class UserIMDtoConverter {

    /**
     * 将UserDto转换成IM使用的UserInfoDto.
     *
     * @param userDto 用户信息
     * @return 不设置createTime和updateTime
     */
    public static UserInfoDto convert(UserDto userDto) {
        UserInfoDto userIMDto = new UserInfoDto();
        userIMDto.setUserId(userDto.getId());
        userIMDto.setUserType(userDto.getUserType());
        userIMDto.setGender(userDto.getGender() == null ? 0 : userDto.getGender());
        userIMDto.setShowName(userDto.getShowName());
        userIMDto.setShowHead(userDto.getShowHead());
        userIMDto.setAvailable(userDto.isAvailable());

        if (SimpleRegexUtils.isMobile(userDto.getUserPhone())) {
            userIMDto.setPhone(userDto.getUserPhone());
        }
        userIMDto.setPublicPhone(true); // 默认公开电话号码

        if (userDto instanceof EmployeeDto) {
            EmployeeDto employeeDto = (EmployeeDto) userDto;
            userIMDto.setCompanyId(employeeDto.getCompanyId());
            userIMDto.setPublicPhone(employeeDto.isPublicPhone()); // 业者可以设置是否公开电话号码
            userIMDto.setUserDetailType(employeeDto.getUserDetailType());
            userIMDto.setUserTitle(EmployeeType.getEnum(employeeDto.getUserDetailType()).getDesc());
        } else if (userDto instanceof MerchantDto) {
            MerchantDto merchantDto = (MerchantDto) userDto;
            userIMDto.setCompanyId(merchantDto.getCompanyId());
            userIMDto.setUserDetailType(merchantDto.getUserDetailType());
            userIMDto.setUserTitle(""); // 暂无
        } else if (userDto instanceof ManagerDto) {
            ManagerDto managerDto = (ManagerDto) userDto;
            userIMDto.setUserDetailType(managerDto.getUserDetailType());
            userIMDto.setUserTitle(ManagerType.getEnum(managerDto.getUserDetailType()).getDesc());
        }

        return userIMDto;
    }

}