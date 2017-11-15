/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.support;

import java.util.Calendar;

import javax.annotation.Resource;

import com.pos.user.constant.UserType;
import com.pos.user.dao.*;
import com.pos.user.domain.*;
import com.pos.user.dto.UserDto;
import com.pos.user.dto.converter.UserDtoConverter;
import com.pos.user.dto.converter.UserIMDtoConverter;
import com.pos.user.dto.employee.EmployeeDto;
import com.pos.user.dto.manager.ManagerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.im.dto.user.UserInfoDto;
import com.pos.im.exception.IMServerException;
import com.pos.im.service.IMUserService;

/**
 * 用户服务支撑类, 仅供内部服务调用.
 *
 * @author wayne
 * @version 1.0, 2016/6/30
 */
@Component
public class UserServiceSupport {

    private Logger logger = LoggerFactory.getLogger(UserServiceSupport.class);

    @Resource
    private UserDao userDao;

    @Resource
    private UserClassDao userClassDao;

    @Resource
    private CustomerDao customerDao;

    @Resource
    private EmployeeDao employeeDao;

    @Resource
    private MerchantDao merchantDao;

    @Resource
    private ManagerDao managerDao;

    @Resource
    private IMUserService imUserService;

    /**
     * 根据用户ID查询指定类型的账号信息.
     *
     * @param userId   用户ID
     * @param userType 用户类型
     * @param disable  是否返回被禁用的账号
     * @param deleted  是否返回被删除的账号
     * @return 根据用户类型手动转型为具体类型(UserDto subclass)
     */
    public UserDto findById(Long userId, UserType userType, boolean disable, boolean deleted) {
        UserDto userDto;
        if (UserType.CUSTOMER == userType) {
            userDto = customerDao.findCustomerByUserId(userId, deleted ? null : false, disable ? null : true);
        } else if (UserType.EMPLOYEE == userType) {
            userDto = employeeDao.findEmployeeByUserId(userId, deleted ? null : false, disable ? null : true);
        } else if (UserType.BUSINESS == userType) {
            userDto = merchantDao.findMerchantByUserId(userId, deleted ? null : false, disable ? null : true);
        } else if (UserType.MANAGER == userType) {
            userDto = managerDao.findManagerByUserId(userId, deleted ? null : false, disable ? null : true);
        } else {
            throw new IllegalParamException("不支持的用户类型！userType: " + userType);
        }
        if (userDto != null) {
            userDto.setImUid(imUserService.buildNeteaseIMAccid(userDto.buildUserIdentifier()));
        }

        return userDto;
    }

    /**
     * 根据用户名查询指定类型的账号信息.
     *
     * @param userName 用户名
     * @param userType 用户类型
     * @param disable  是否返回被禁用的账号
     * @param deleted  是否返回被删除的账号
     * @return 根据用户类型手动转型为具体类型(UserDto subclass)
     */
    public UserDto findByUserName(String userName, UserType userType, boolean disable, boolean deleted) {
        UserDto userDto;
        if (UserType.CUSTOMER == userType) {
            userDto = customerDao.findCustomerByUserName(userName, deleted ? null : false, disable ? null : true);
        } else if (UserType.EMPLOYEE == userType) {
            userDto = employeeDao.findEmployeeByUserName(userName, deleted ? null : false, disable ? null : true);
        } else if (UserType.BUSINESS == userType) {
            userDto = merchantDao.findMerchantByUserName(userName, deleted ? null : false, disable ? null : true);
        } else if (UserType.MANAGER == userType) {
            userDto = managerDao.findManagerByUserName(userName, deleted ? null : false, disable ? null : true);
        } else {
            throw new IllegalParamException("不支持的用户类型！userType: " + userType);
        }

        if (userDto != null) {
            userDto.setImUid(imUserService.buildNeteaseIMAccid(userDto.buildUserIdentifier()));
        }

        return userDto;
    }

    /**
     * 根据手机号查询指定类型的账号信息.
     *
     * @param userPhone 手机号
     * @param userType  用户类型
     * @param disable   是否返回被禁用的账号
     * @param deleted   是否返回被删除的账号
     * @return 根据用户类型手动转型为具体类型(UserDto subclass)
     */
    public UserDto findByUserPhone(String userPhone, UserType userType, boolean disable, boolean deleted) {
        UserDto user;
        if (UserType.CUSTOMER == userType) {
            user = customerDao.findCustomerByUserPhone(userPhone, deleted ? null : false, disable ? null : true);
        } else if (UserType.EMPLOYEE == userType) {
            user = employeeDao.findEmployeeByUserPhone(userPhone, deleted ? null : false, disable ? null : true);
        } else if (UserType.BUSINESS == userType) {
            user = merchantDao.findMerchantByUserPhone(userPhone, deleted ? null : false, disable ? null : true);
        } else if (UserType.MANAGER == userType) {
            user = managerDao.findManagerByUserPhone(userPhone, deleted ? null : false, disable ? null : true);
        } else {
            throw new IllegalParamException("不支持的用户类型！userType: " + userType);
        }
        if (user != null) {
            user.setImUid(imUserService.buildNeteaseIMAccid(user.buildUserIdentifier()));
        }
        return user;
    }

    /**
     * 根据既定的user和userClass实体查询用户账号信息, 并填充成UserDto返回.
     *
     * @param user 用户信息，不能为空
     * @param uc   用户类型信息，不能为空
     * @return 根据用户类型手动转型为具体类型(UserDto subclass)
     */
    public UserDto findAndFillUserDto(User user, UserClass uc) {
        UserType userType = UserType.getEnum(uc.getUserType());
        UserDto userDto = null;
        if (userType == UserType.CUSTOMER) {
            Customer customer = customerDao.getByUserId(user.getId());
            if (customer != null) {
                userDto = UserDtoConverter.convert2CustomerDto(user, uc, customer);
            }
        } else if (userType == UserType.EMPLOYEE) {
            Employee employee = employeeDao.getByUserId(user.getId());
            if (employee != null) {
                userDto = UserDtoConverter.convert2EmployeeDto(user, uc, employee);
            }
        } else if (userType == UserType.BUSINESS) {
            Merchant merchant = merchantDao.getByUserId(user.getId());
            if (merchant != null) {
                userDto = UserDtoConverter.convert2MerchantDto(user, uc, merchant);
            }
        } else if (userType == UserType.MANAGER) {
            Manager manager = managerDao.getByUserId(user.getId());
            if (manager != null) {
                userDto = UserDtoConverter.convert2ManagerDto(user, uc, manager);
            }
        }
        if (userDto != null) {
            UserInfoDto userInfoDto = imUserService.findUser(user.getId(), userType.getValue(), true);
            if (userInfoDto == null) {
                // 用户在注册创建IM账号失败，为用户重新创建账号
                refresh2IMServer(userDto);
            }
            userDto.setImUid(imUserService.buildNeteaseIMAccid(userDto.buildUserIdentifier()));
        }
        return userDto;
    }

    /**
     * 更新用户信息前检查字段完整性.
     *
     * @param beforeDto 修改前的用户数据(UserDto subclass)
     * @param afterDto  修改后的用户数据(UserDto subclass)
     * @throws ValidationException 未设置不能为空的字段, 或者字段值不符合约定
     */
    public void checkUpdate(UserDto beforeDto, UserDto afterDto) {
        FieldChecker.checkEmpty(beforeDto, "beforeDto");
        FieldChecker.checkEmpty(afterDto, "afterDto");
        beforeDto.check("beforeDto");
        afterDto.check("afterDto");
    }

    /**
     * 更新用户信息, 仅当数据有修改时才执行update, 调用前确保已经执行过checkUpdate.
     *
     * @param beforeDto 修改前的用户数据(UserDto subclass)
     * @param afterDto  修改后的用户数据(UserDto subclass)
     * @return 数据有被更新返回true, 否则返回false
     */
    public boolean updateUser(UserDto beforeDto, UserDto afterDto) {
        User before = UserDtoConverter.convert2User(beforeDto);
        User after = UserDtoConverter.convert2User(afterDto);
        if (!JsonUtils.objectToJson(before).equals(JsonUtils.objectToJson(after))) {
            userDao.update(after);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新用户类型信息, 仅当数据有修改时才执行update, 调用前确保已经执行过checkUpdate.
     *
     * @param beforeDto 修改前的用户数据(UserDto subclass)
     * @param afterDto  修改后的用户数据(UserDto subclass)
     * @param force     是否强制更新
     * @return 数据有被更新返回true, 否则返回false
     */
    public boolean updateUserClass(UserDto beforeDto, UserDto afterDto, boolean force) {
        if (force) {
            UserClass after = UserDtoConverter.convert2UserClass(afterDto);
            after.setUpdateTime(Calendar.getInstance().getTime());
            userClassDao.update(after);
            return true;
        } else {
            UserClass before = UserDtoConverter.convert2UserClass(beforeDto);
            UserClass after = UserDtoConverter.convert2UserClass(afterDto);
            if (!JsonUtils.objectToJson(before).equals(JsonUtils.objectToJson(after))) {
                after.setUpdateTime(Calendar.getInstance().getTime());
                userClassDao.update(after);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 将用户信息刷新到IMServer.
     *
     * @param userDto 用户信息(UserDto subclass)
     * @return 刷新成功返回true, 刷新失败返回false
     */
    public boolean refresh2IMServer(UserDto userDto) {
        UserInfoDto imDto = UserIMDtoConverter.convert(userDto);
        try {
            //imUserService.refresh(imDto);
            imUserService.createIMUser(imDto);
            userDto.setImUid(imDto.getImUid());
            return true;
        } catch (IMServerException e) {
            logger.error("刷新用户信息到IM Server失败！" + imDto.toString(), e);
            return false;
        }
    }

    /**
     * 将用户信息刷新到IMServer, 仅当数据有修改时才执行refresh.
     *
     * @param beforeDto 修改前的用户数据(UserDto subclass)
     * @param afterDto  修改后的用户数据(UserDto subclass)
     * @return 刷新成功返回true, 刷新失败或者没有刷新返回false
     */
    public boolean refresh2IMServer(UserDto beforeDto, UserDto afterDto) {
        UserInfoDto before = UserIMDtoConverter.convert(beforeDto);
        UserInfoDto after = UserIMDtoConverter.convert(afterDto);
        if (!JsonUtils.objectToJson(before).equals(JsonUtils.objectToJson(after))) {
            try {
                //imUserService.refresh(after);
                imUserService.updateIMUser(after);
                return true;
            } catch (IMServerException e) {
                logger.error("刷新(CAS)用户信息到IM Server失败！" + after.toString(), e);
            }
        }
        return false;
    }

    /**
     * 获取用户的细分类型
     *
     * @param user 用户信息
     * @return 有相应细分类型的用户返回其对应的细分类型，没有则返回null
     */
    public Byte getUserDetailType(UserDto user) {
        if (user instanceof ManagerDto) {
            return ((ManagerDto) user).getUserDetailType();
        } else if (user instanceof EmployeeDto) {
            return ((EmployeeDto) user).getUserDetailType();
        } else {
            return null;
        }
    }

}
