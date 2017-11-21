/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.converter;

import com.pos.user.domain.Customer;
import com.pos.user.domain.Manager;
import com.pos.user.domain.User;
import com.pos.user.domain.UserClass;
import com.pos.user.dto.UserDto;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.manager.ManagerDto;

/**
 * 用户信息的Entity - DTO转换器.
 *
 * @author wayne
 * @version 1.0, 2016/8/2
 */
public class UserDtoConverter {

    public static User convert2User(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUserName(userDto.getUserName());
        user.setUserPhone(userDto.getUserPhone());
        user.setPassword(userDto.getPassword());
        user.setDeleted(userDto.isDeleted());
        user.setName(userDto.getName());
        user.setMail(userDto.getMail());
        user.setGender(userDto.getGender());
        user.setAge(userDto.getAge());
        user.setIdCard(userDto.getIdCard());
        user.setIdImageA(userDto.getIdImageA());
        user.setIdImageB(userDto.getIdImageB());
        user.setIdHoldImage(userDto.getIdHoldImage());
        user.setIdHoldImageB(userDto.getIdHoldImageB());
        return user;
    }

    public static UserClass convert2UserClass(UserDto userDto) {
        UserClass uc = new UserClass();
        uc.setId(userDto.getUserTypeId());
        uc.setUserId(userDto.getId());
        uc.setUserType(userDto.getUserType());
        uc.setAvailable(userDto.isAvailable());
        uc.setCreateUserId(userDto.getCreateUserId());
        uc.setCreateTime(userDto.getCreateTime());
        uc.setUpdateTime(userDto.getUpdateTime());
        uc.setLastLoginTime(userDto.getLastLoginTime());
        uc.setRegisterIp(userDto.getRegisterIp());
        uc.setRegisterAddress(userDto.getRegisterAddress());
        uc.setLastLoginIp(userDto.getLoginIp());
        uc.setLoginAddress(userDto.getLoginAddress());
        return uc;
    }

    public static Customer convert2Customer(CustomerDto customerDto) {
        Customer c = new Customer();
        c.setId(customerDto.getEntityId());
        c.setUserId(customerDto.getId());
        c.setNickName(customerDto.getNickName());
        c.setHeadImage(customerDto.getHeadImage());
        return c;
    }

    public static Manager convert2Manager(ManagerDto managerDto) {
        Manager m = new Manager();
        m.setId(managerDto.getEntityId());
        m.setUserId(managerDto.getId());
        m.setUserDetailType(managerDto.getUserDetailType());
        m.setHeadImage(managerDto.getHeadImage());
        m.setQuitJobs(managerDto.isQuitJobs());
        return m;
    }

    public static CustomerDto convert2CustomerDto(User user, UserClass userClass, Customer customer) {
        CustomerDto dto = new CustomerDto();
        fillUserDto(dto, user, userClass);
        dto.setNickName(customer.getNickName());
        dto.setHeadImage(customer.getHeadImage());
        return dto;
    }

    public static ManagerDto convert2ManagerDto(User user, UserClass userClass, Manager manager) {
        ManagerDto dto = new ManagerDto();
        fillUserDto(dto, user, userClass);
        dto.setUserDetailType(manager.getUserDetailType());
        dto.setHeadImage(manager.getHeadImage());
        dto.setQuitJobs(manager.isQuitJobs());
        return dto;
    }

    private static void fillUserDto(UserDto userDto, User user, UserClass userClass) {
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setUserPhone(user.getUserPhone());
        userDto.setDeleted(user.isDeleted());
        userDto.setName(user.getName());
        userDto.setMail(user.getMail());
        userDto.setGender(user.getGender());
        userDto.setIdCard(user.getIdCard());
        userDto.setIdImageA(user.getIdImageA());
        userDto.setIdImageB(user.getIdImageB());
        userDto.setIdHoldImage(user.getIdHoldImage());

        userDto.setUserType(userClass.getUserType());
        userDto.setAvailable(userClass.isAvailable());
        userDto.setCreateUserId(userClass.getCreateUserId());
        userDto.setCreateTime(userClass.getCreateTime());
        userDto.setUpdateTime(userClass.getUpdateTime());
        userDto.setLastLoginTime(userClass.getLastLoginTime());
    }

}