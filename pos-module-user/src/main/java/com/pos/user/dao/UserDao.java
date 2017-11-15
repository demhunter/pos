/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.user.condition.query.UserListCondition;
import com.pos.user.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户相关DAO.
 *
 * @author wayne
 * @version 1.0, 2016/6/8
 */
@Repository
public interface UserDao {

    void save(@Param("user") User user);

    void update(@Param("user") User user);

    User getById(@Param("userId") Long userId);

    User getByUserName(@Param("userName") String userName);

    User getByUserPhone(@Param("userPhone") String userPhone);

    /**
     * 统计身份证号被使用的次数
     *
     * @param idNumber 身份证号
     * @return 查询结果
     */
    int countIdNumber(@Param("idNumber") String idNumber);

    /**
     * 根据查询条件查询用户列表
     *
     * @param condition 查询条件
     * @return 用户ID列表
     */
    List<Long> queryUserIds(@Param("condition") UserListCondition condition);

}