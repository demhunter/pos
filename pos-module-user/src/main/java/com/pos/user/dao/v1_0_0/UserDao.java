/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao.v1_0_0;

import com.pos.user.domain.v1_0_0.User;
import com.pos.user.domain.v1_0_0.UserExtension;
import org.apache.ibatis.annotations.Param;

/**
 * 用户相关DAO
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public interface UserDao {

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     */
    void save(@Param("user") User user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    void update(@Param("user") User user);

    /**
     * 保存用户拓展信息
     *
     * @param extension 拓展信息
     */
    void saveExtension(@Param("extension") UserExtension extension);

    /**
     * 获取用户拓展信息
     *
     * @param userId 用户userId
     * @return 用户拓展信息
     */
    UserExtension getExtension(@Param("userId") Long userId);

    /**
     * 更新用户拓展信息
     *
     * @param extension 用户拓展信息
     */
    void updateExtension(@Param("extension") UserExtension extension);

    /**
     * 根据电话号码查询客户账号信息
     *
     * @param phone 电话号码
     * @return 用户信息
     */
    User getByUserPhone(@Param("phone") String phone);

    /**
     * 根据登录账号查询用户账号信息
     *
     * @param loginName 登录账号
     * @param userType  登录账号类型
     * @return 用户账号信息
     */
    User getByLoginName(@Param("loginName") String loginName, @Param("userType") String userType);

    /**
     * 根据电话号码和账号类型查询用户账号信息
     *
     * @param phone    电话号码
     * @param userType 用户类型
     * @return 用户账号信息
     */
    User getByPhoneAndType(@Param("phone") String phone, @Param("userType") String userType);
}
